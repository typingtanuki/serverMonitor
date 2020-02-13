package com.github.typingtanuki.servermonitor.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.connectors.Connector;
import com.github.typingtanuki.servermonitor.connectors.LoggerConnector;
import com.github.typingtanuki.servermonitor.connectors.teams.TeamsConnector;
import com.github.typingtanuki.servermonitor.monitors.*;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.updates.UpdateChecker;
import com.github.typingtanuki.servermonitor.web.WebServer;
import com.github.typingtanuki.servermonitor.web.WwwServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    private StatusManager statusManager;
    private MainConfig config;
    private List<Monitor> monitors = new LinkedList<>();
    private List<Connector> connectors;

    public ServerMonitor() {
        super();
        info = new SystemInfo();
    }

    public void startMonitoring() throws InterruptedException {
        synchronized (configLock) {
            monitors.add(new HandshakeMonitor(config));
            monitors.add(new DiskMonitor(config));
            monitors.add(new CpuMonitor(config));
            monitors.add(new MemoryMonitor(config));
            monitors.add(new ProcessMonitor(config));
            monitors.add(new PingMonitor(config));
            monitors.add(UpdateChecker.bestChecker(config));
        }

        while (true) {
            List<MonitorReport> reports = new LinkedList<>();
            synchronized (configLock) {
                for (Monitor monitor : monitors) {
                    if (monitor.isEnabled()) {
                        reports.addAll(monitor.monitor(info));
                    }
                }
            }
            handleReports(reports);

            Thread.sleep(config.getMonitorTime());
        }
    }

    private void handleReports(List<MonitorReport> reports) {
        List<MonitorReport> failed = new LinkedList<>();
        List<MonitorReport> succeeded = new LinkedList<>();

        statusManager.updateStatus(reports);

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
        if (config.getTeamsHook() != null) {
            connectors.add(new TeamsConnector(config, info));
        }
        return connectors;
    }

    public void loadConfig() throws IOException {
        synchronized (configLock) {
            Path configPath = Paths.get("./conf/monitor.json");
            if (!Files.exists(configPath)) {
                logger.warn("Missing config file in " + configPath.toFile().getAbsolutePath());
                System.exit(1);
            }
            config = new ObjectMapper()
                    .enable(JsonParser.Feature.ALLOW_COMMENTS)
                    .readerFor(MainConfig.class)
                    .readValue(Files.readString(configPath, StandardCharsets.UTF_8));
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

    public MainConfig updateConfig(MainConfig newConfig, boolean persist) throws IOException {
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
}
