package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.connectors.Connector;
import com.github.typingtanuki.servermonitor.connectors.TeamsConnector;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.web.WebServer;
import oshi.SystemInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @author clerc
 * @since 2020/01/24
 */
public class ServerMonitor {
    private final SystemInfo info;
    private final MonitorConfig config;
    private List<Monitor> monitors = new LinkedList<>();

    public ServerMonitor() {
        super();
        info = new SystemInfo();
        config = new MonitorConfig();
    }

    public void startMonitoring() throws InterruptedException {
        config.validate();

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

        while (true) {
            System.out.println("-----------------");

            List<MonitorReport> reports = new LinkedList<>();
            for (Monitor monitor : monitors) {
                reports.addAll(monitor.monitor(info));
            }

            handleReports(reports);

            Thread.sleep(config.monitorTime());
        }
    }

    private void handleReports(List<MonitorReport> reports) {
        List<MonitorReport> failed = new LinkedList<>();

        for (MonitorReport report : reports) {
            if (report.isOK()) {
                System.out.println("OK: " + report.shortDescription());
            } else {
                System.out.println("NG: " + report.shortDescription());
                failed.add(report);
            }
        }

        if (!failed.isEmpty()) {
            warnIssue(failed);
        }
    }

    private void warnIssue(List<MonitorReport> failedMonitorReports) {
        if (config.teamsHook() == null) {
            return;
        }

        Connector connector = new TeamsConnector(config, info);
        for (MonitorReport failedMonitorReport : failedMonitorReports) {
            connector.reportFailure(failedMonitorReport);
        }
    }

    public void loadConfig() throws IOException {
        Path configPath = Paths.get("./conf/monitor.conf");
        System.out.println(configPath.toFile().getAbsolutePath());
        if (!Files.exists(configPath)) {
            System.err.println("Missing config file");
            System.exit(1);
        }
        config.from(Files.readAllLines(configPath));
    }

    public void startServer() throws IOException {
        WebServer server = new WebServer(config);
        server.startServer();
        server.start();
    }
}
