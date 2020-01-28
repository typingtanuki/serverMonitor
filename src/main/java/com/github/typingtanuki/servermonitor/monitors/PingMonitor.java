package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.PingMonitorReport;
import oshi.SystemInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

public class PingMonitor implements Monitor {
    private MonitorConfig config;

    public PingMonitor(MonitorConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<String> ping = config.ping();

        List<MonitorReport> out = new LinkedList<>();
        for (String server : ping) {
            PingMonitorReport report = new PingMonitorReport(server);
            try {
                InetAddress target = InetAddress.getByName(server);
                if (!target.isReachable(5000)) {
                    throw new IOException("Server unreachable");
                }
                report.ok();
            } catch (IOException | RuntimeException e) {
                report.ng();
            }
            out.add(report);
        }
        return out;
    }
}
