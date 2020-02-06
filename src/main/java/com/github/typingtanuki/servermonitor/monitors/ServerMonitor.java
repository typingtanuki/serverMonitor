package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.connectors.Connector;
import com.github.typingtanuki.servermonitor.connectors.LoggerConnector;
import com.github.typingtanuki.servermonitor.connectors.teams.TeamsConnector;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.updates.UpdateChecker;
import com.github.typingtanuki.servermonitor.web.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private final MonitorConfig config;
    private List<Monitor> monitors = new LinkedList<>();
    private List<Connector> connectors;

    public ServerMonitor() {
        super();
        info = new SystemInfo();
        config = new MonitorConfig();
    }

    public void startMonitoring() throws InterruptedException {
        if (!config.handshake().isEmpty()) {
            monitors.add(new HandshakeMonitor(config));
        }
        monitors.add(new DiskMonitor(config));
        monitors.add(new CpuMonitor(config));
        monitors.add(new MemoryMonitor(config));
        if (!config.processes().isEmpty()) {
            monitors.add(new ProcessMonitor(config));
        }
        if (!config.ping().isEmpty()) {
            monitors.add(new PingMonitor(config));
        }

        UpdateChecker updateChecker = UpdateChecker.bestChecker(config.checkUpdates());

        while (true) {
            List<MonitorReport> reports = new LinkedList<>();
            for (Monitor monitor : monitors) {
                reports.addAll(monitor.monitor(info));
            }
            if (updateChecker != null) {
                reports.add(updateChecker.check());
            }

            handleReports(reports);

            Thread.sleep(config.monitorTime());
        }
    }

    private void handleReports(List<MonitorReport> reports) {
        List<MonitorReport> failed = new LinkedList<>();

        for (MonitorReport report : reports) {
            if (report.isOK()) {
                logger.debug("OK: {}", report.shortDescription());
            } else {
                logger.debug("NG: {}", report.shortDescription());
                failed.add(report);
            }
        }

        if (!failed.isEmpty()) {
            warnIssue(failed);
        }
    }

    private void warnIssue(List<MonitorReport> failedMonitorReports) {
        List<Connector> connectors = initConnectors(config);

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
        if (config.teamsHook() != null) {
            connectors.add(new TeamsConnector(config, info));
        }
        return connectors;
    }

    public void loadConfig() throws IOException {
        Path configPath = Paths.get("./conf/monitor.conf");
        if (!Files.exists(configPath)) {
            logger.warn("Missing config file in " + configPath.toFile().getAbsolutePath());
            System.exit(1);
        }
        config.from(Files.readAllLines(configPath));
        config.validate();
    }

    public void startServer() throws IOException {
        WebServer server = new WebServer(config);
        server.startServer();
        server.start();
    }
}
