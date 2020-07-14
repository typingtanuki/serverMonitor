package com.github.typingtanuki.servermonitor.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.typingtanuki.servermonitor.ZipSigner;
import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.connectors.Connector;
import com.github.typingtanuki.servermonitor.connectors.LoggerConnector;
import com.github.typingtanuki.servermonitor.connectors.teams.TeamsConnector;
import com.github.typingtanuki.servermonitor.monitors.*;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.updates.UpdateChecker;
import com.github.typingtanuki.servermonitor.web.WebServer;
import com.github.typingtanuki.servermonitor.web.WwwServer;
import com.github.typingtanuki.servermonitor.web.status.ShortStatusResponse;
import com.github.typingtanuki.servermonitor.web.status.SimpleStatus;
import com.github.typingtanuki.servermonitor.web.status.SimpleStatusResponse;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.typingtanuki.servermonitor.utils.SimpleStack.simpleStack;

/**
 * Core of the monitoring
 *
 * @author clerc
 * @since 2020/01/24
 */
public class ServerMonitor {
   private static final Logger logger = LoggerFactory.getLogger(ServerMonitor.class);
   private final SystemInfo info;
   private final WwwServer wwwServer = new WwwServer();
   private final Object configLock = new Object[0];
   private final Map<String, Long> triggered = new LinkedHashMap<>();
   private final ExecutorService slowExecutor;
   private final ExecutorService areaExecutor;
   private StatusManager statusManager;
   private MainConfig config;
   private final List<Monitor> fastMonitors = new LinkedList<>();
   private final List<Monitor> slowMonitors = new LinkedList<>();
   private List<Connector> connectors;
   private boolean slowMonitorsRunning = false;
   private boolean pingingArea = false;

   public ServerMonitor() {
      super();
      info = new SystemInfo();

      slowExecutor = new ThreadPoolExecutor(
            1, 1, 0, TimeUnit.MILLISECONDS, new BlockingArrayQueue<>()
      );
      areaExecutor = new ThreadPoolExecutor(
            1, 1, 0, TimeUnit.MILLISECONDS, new BlockingArrayQueue<>()
      );
   }

   public void startMonitoring() throws InterruptedException {
      synchronized (configLock) {
         fastMonitors.add(new DiskMonitor(config));
         fastMonitors.add(new CpuMonitor(config));
         fastMonitors.add(new MemoryMonitor(config));
         fastMonitors.add(new ProcessMonitor(config));

         slowMonitors.add(new HandshakeMonitor(config));
         slowMonitors.add(new PingMonitor(config));
         slowMonitors.add(new NetworkMonitor(config));
         slowMonitors.add(UpdateChecker.bestChecker(config));
      }

      long lastRun = 0;
      while (true) {
         if (new Date().getTime() - lastRun > config.getMonitorTime()) {
            pingFastMonitors();
            pingSlowMonitors();
            pingArea();
            lastRun = new Date().getTime();
         }
         Thread.sleep(100);
      }
   }

   private void pingFastMonitors() {
      runMonitors(fastMonitors, true);
   }

   private synchronized void pingSlowMonitors() {
      if (slowMonitorsRunning) {
         return;
      }

      slowMonitorsRunning = true;
      slowExecutor.submit(() -> {
         runMonitors(slowMonitors, false);
         slowMonitorsRunning = false;
      });
   }

   private synchronized void pingArea() {
      if (pingingArea) {
         return;
      }
      List<String> remotes;
      synchronized (configLock) {
         remotes = new ArrayList<>(config.getHandshake().getMonitoring());
      }

      pingingArea = true;
      areaExecutor.submit(() -> {
         scanArea(remotes);
         pingingArea = false;
      });
   }

   private void scanArea(List<String> remotes) {
      Map<String, SimpleStatus> areaStatus = new LinkedHashMap<>();
      Map<String, Map<MonitorType, Boolean>> areaShortStatus = new LinkedHashMap<>();

      Set<String> allRemotes = new LinkedHashSet<>(remotes);
      allRemotes.add("localhost:" + config.getPort());

      for (String remote : allRemotes) {
         ShortStatusResponse shortStatus = getRemoteStatus(remote);
         SimpleStatusResponse advanced = getAdvancedRemoteStatus(remote);
         if (advanced != null) {
            areaStatus.put(advanced.getIdentity(), advanced.getStatus());
         }
         if (shortStatus != null) {
            areaShortStatus.put(shortStatus.getIdentity(), shortStatus.getStatus());
         } else {
            areaShortStatus.put(remote, Map.of(MonitorType.handshake, Boolean.FALSE));
         }
      }

      statusManager.updateAreaStatus(areaStatus);
      statusManager.updateAreaShortStatus(areaShortStatus);
   }

   private SimpleStatusResponse getAdvancedRemoteStatus(String remote) {
      RestCall<SimpleStatusResponse> call =
            new RestCall<>(remote, "/status?skipHistory", SimpleStatusResponse.class);
      try {
         return call.get(1000);
      } catch (RestCallException e) {
         logger.debug("Could not get remote status: {}\r\n{}", remote, simpleStack(e));
         return null;
      }
   }

   private ShortStatusResponse getRemoteStatus(String remote) {
      RestCall<ShortStatusResponse> call =
            new RestCall<>(remote, "/status/short", ShortStatusResponse.class);
      try {
         return call.get(1000);
      } catch (RestCallException e) {
         logger.debug("Could not get remote status: {}\r\n{}", remote, simpleStack(e));
         return null;
      }
   }

   private void runMonitors(List<Monitor> monitors, boolean isFast) {
      List<MonitorReport> reports = new LinkedList<>();
      synchronized (configLock) {
         for (Monitor monitor : monitors) {
            if (monitor.isEnabled()) {
               reports.addAll(monitor.monitor(info));
            }
         }
      }
      handleReports(reports, isFast);
   }

   private void handleReports(List<MonitorReport> reports, boolean isFast) {
      List<MonitorReport> failed = new LinkedList<>();
      List<MonitorReport> succeeded = new LinkedList<>();

      if (isFast) {
         statusManager.updateStatus(reports, null);
      } else {
         statusManager.updateStatus(null, reports);
      }
      for (MonitorReport report : reports) {
         if (report.isOK()) {
            logger.debug("OK: {}", report.getDescription());
            succeeded.add(report);
         } else {
            logger.debug("NG: {}", report.getDescription());
            failed.add(report);
         }
      }

      if (!failed.isEmpty()) {
         warnIssue(failed, succeeded);
      }
   }

   private void warnIssue(List<MonitorReport> failedMonitorReports,
                          List<MonitorReport> succeededMonitorReports) {
      List<Connector> connectors;
      long debounceTime;

      synchronized (configLock) {
         connectors = initConnectors(config);
         debounceTime = config.getDebounceTime();
      }

      long now = System.currentTimeMillis();
      for (MonitorReport failedMonitorReport : failedMonitorReports) {
         String monitorKey = failedMonitorReport.monitorKey();
         Long lastTrigger = triggered.get(monitorKey);
         if (lastTrigger != null && lastTrigger < now - debounceTime) {
            // Debounced
            continue;
         }
         triggered.put(monitorKey, now);
         for (Connector connector : connectors) {
            connector.reportFailure(failedMonitorReport);
         }
      }

      // Check for recovery
      for (MonitorReport succeededMonitorReport : succeededMonitorReports) {
         String monitorKey = succeededMonitorReport.monitorKey();
         Long lastTrigger = triggered.remove(monitorKey);
         if (lastTrigger != null) {
            // Recovered
            for (Connector connector : connectors) {
               connector.reportRecovery(succeededMonitorReport);
            }
         }
      }
   }

   private synchronized List<Connector> initConnectors(MainConfig config) {
      if (connectors != null) {
         return new ArrayList<>(connectors);
      }
      connectors = new LinkedList<>();
      connectors.add(new LoggerConnector());
      if (config.getTeamsHook() != null && !config.getTeamsHook().isBlank()) {
         connectors.add(new TeamsConnector(config, info));
      }
      return connectors;
   }

   public void loadConfig() throws IOException {
      synchronized (configLock) {
         Path configPath = Paths.get("./conf/monitor.json");
         if (!Files.exists(configPath)) {
            logger.warn("Missing config file in " +
                        configPath.toFile().getAbsolutePath());
            System.exit(1);
         }
         String fileContent = Files.readString(configPath, StandardCharsets.UTF_8);
         config = new ObjectMapper()
               .enable(JsonParser.Feature.ALLOW_COMMENTS)
               .readerFor(MainConfig.class)
               .readValue(fileContent);
         config.validate();
         this.statusManager = new StatusManager(config);
      }
   }

   public void startServer() throws IOException {
      WebServer server = new WebServer(config);
      server.startServer();
      server.start();
   }

   public StatusManager currentStatus() {
      return statusManager;
   }

   public MainConfig currentConfig() {
      return config;
   }

   public MainConfig updateConfig(MainConfig newConfig, boolean persist)
         throws IOException {
      synchronized (configLock) {
         newConfig.validate();
         newConfig.copyTo(config);
         if (persist) {
            persistConfig();
         }
         return config;
      }
   }

   private void persistConfig() throws IOException {
      try {
         String jsonConfig = new ObjectMapper()
               .writerFor(MainConfig.class)
               .withDefaultPrettyPrinter()
               .writeValueAsString(config);
         Path configPath = Paths.get("./conf/monitor.json");
         Files.writeString(configPath, jsonConfig, StandardCharsets.UTF_8);
      } catch (JsonProcessingException e) {
         throw new IOException("Could not convert config object to JSON", e);
      }
   }

   public WwwServer wwwServer() {
      return wwwServer;
   }

   public String doUpdate() {
      return UpdateChecker.doUpdate();
   }

   public String updateMonitor(Path uploadedZip, Path uploadedCert) throws IOException {
      ZipSigner.checkSign(uploadedZip, uploadedCert);
      return "Certificate OK";
   }
}
