package com.github.typingtanuki.servermonitor.monitors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.connectors.Connector;
import com.github.typingtanuki.servermonitor.connectors.LoggerConnector;
import com.github.typingtanuki.servermonitor.connectors.teams.TeamsConnector;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.Status;
import com.github.typingtanuki.servermonitor.updates.UpdateChecker;
import com.github.typingtanuki.servermonitor.web.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Core of the monitoring
 *
 * @author clerc
 * @since 2020/01/24
 */
public class ServerMonitor {
    private static final Logger logger = LoggerFactory.getLogger(ServerMonitor.class);

    private final SystemInfo info;
    private final Object configLock = new Object[0];
    private MonitorConfig config;
    private List<Monitor> monitors = new LinkedList<>();
    private List<Connector> connectors;
    private Status status;

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

        updateStatus(reports);

        for (MonitorReport report : reports) {
            if (report.isOK()) {
                logger.debug("OK: {}", report.getDescription());
            } else {
                logger.debug("NG: {}", report.getDescription());
                failed.add(report);
            }
        }

        if (!failed.isEmpty()) {
            warnIssue(failed);
        }
    }

    private void warnIssue(List<MonitorReport> failedMonitorReports) {
        List<Connector> connectors;
        synchronized (configLock) {
            connectors = initConnectors(config);
        }

        for (MonitorReport failedMonitorReport : failedMonitorReports) {
            for (Connector connector : connectors) {
                connector.reportFailure(failedMonitorReport);
            }
        }
    }

    private synchronized List<Connector> initConnectors(MonitorConfig config) {
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
                    .readerFor(MonitorConfig.class)
                    .readValue(Files.readString(configPath, StandardCharsets.UTF_8));
            config.validate();
        }
    }

    public void startServer() throws IOException {
        WebServer server = new WebServer(config);
        server.startServer();
        server.start();
    }

    private synchronized void updateStatus(List<MonitorReport> reports) {
        status = new Status(reports);
    }

    public synchronized Status getStatus() {
        if (status == null) {
            return new Status(Collections.emptyList());
        }
        return status;
    }

    public MonitorConfig fetchSettings() {
        return config;
    }

    public MonitorConfig updateSettings(MonitorConfig newConfig, boolean persist) throws IOException {
        synchronized (configLock) {
            newConfig.validate();
            newConfig.copyTo(config);
            if (persist) {
                saveConfig();
            }
            return config;
        }
    }

    private void saveConfig() throws IOException {
        try {
            String jsonConfig = new ObjectMapper()
                    .writerFor(MonitorConfig.class)
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(config);
            Path configPath = Paths.get("./conf/monitor.json");
            Files.writeString(configPath, jsonConfig, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new IOException("Could not convert config object to JSON", e);
        }
    }
}
