package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.PingMonitorReport;
import oshi.SystemInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * Monitors connectivity to other servers using ping (echo based)
 */
public class PingMonitor implements Monitor {
    private MainConfig config;

    public PingMonitor(MainConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<String> ping = config.getPing().getMonitoring();

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

    @Override
    public boolean isEnabled() {
        if (!config.getPing().isEnabled()) {
            return false;
        }
        List<String> monitoring = config.getPing().getMonitoring();
        return monitoring != null && !monitoring.isEmpty();
    }

    @Override
    public MonitorType getType() {
        return MonitorType.ping;
    }
}
