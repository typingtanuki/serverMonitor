package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.PingMonitorReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Monitors connectivity to other servers using ping (echo based)
 */
public class PingMonitor implements Monitor {
    private static Logger logger = LoggerFactory.getLogger(PingMonitor.class);
    private final Map<String, String> lastSeen = new LinkedHashMap<>();

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
            out.add(tryPing(server));
        }
        List<String> removed = new LinkedList<>();
        for (String key : lastSeen.keySet()) {
            if (!ping.contains(key)) {
                removed.add(key);
            }
        }
        for (String r : removed) {
            lastSeen.remove(r);
        }
        return out;
    }

    private PingMonitorReport tryPing(String server) {
        PingMonitorReport report = new PingMonitorReport(server);
        if (server.contains(":")) {
            try {
                String host = server.split(":", 2)[0];
                int port = Integer.parseInt(server.split(":", 2)[1]);
                report = new PingMonitorReport(host);
                tryOpenPort(host, report, port);
            } catch (RuntimeException e) {
                report.ng(e, lastSeen.get(server));
            }
            return report;
        }

        String result;
        result = tryEcho(server, report);
        if (result != null) {
            lastSeen.put(server, result);
            return report;
        }

        result = tryOpenPort(server, report, 80);
        if (result != null) {
            lastSeen.put(server, result);
            return report;
        }

        result = tryOpenPort(server, report, 8080);
        if (result != null) {
            lastSeen.put(server, result);
            return report;
        }

        report.ng();
        return report;
    }

    private String tryEcho(String server,
                           PingMonitorReport report) {
        try {
            InetAddress target = InetAddress.getByName(server);
            if (!target.isReachable(5000)) {
                throw new IOException("Server unreachable");
            }
            return report.ok("echo");
        } catch (IOException | RuntimeException e) {
            logger.debug("Could not echo server {}", server, e);
        }
        return null;
    }

    private String tryOpenPort(String server,
                               PingMonitorReport report,
                               int port) {
        Socket s = null;
        try {
            s = new Socket();
            s.setReuseAddress(true);
            SocketAddress sa = new InetSocketAddress(server, port);
            s.connect(sa, 5000);
            return report.ok("Port " + port);
        } catch (IOException e) {
            report.ng(e, lastSeen.get(server));
            logger.debug("Could not connect to {}", server, e);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e2) {
                    logger.warn("Could not close connection to {}", server, e2);
                }
            }
        }
        return null;
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

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.remote;
    }
}
