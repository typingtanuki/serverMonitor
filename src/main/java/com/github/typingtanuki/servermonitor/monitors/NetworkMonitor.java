package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.NetworkMonitorReport;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NetworkMonitor implements Monitor {
    private final MainConfig config;
    private long prevRecv = -1;
    private long prevSent = -1;
    private LinkedList<Long> historyRecv = new LinkedList<>();
    private LinkedList<Long> historySent = new LinkedList<>();

    public NetworkMonitor(MainConfig config) {
        super();
        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        NetworkIF[] nifs = systemInfo.getHardware().getNetworkIFs();
        if (nifs == null) {
            return null;
        }

        long recv = 0;
        long sent = 0;
        for (NetworkIF nif : nifs) {
            recv += nif.getBytesRecv();
            sent += nif.getBytesSent();
        }
        if (prevRecv > -1 && prevSent > -1) {
            long dRecv = recv - prevRecv;
            long dSent = sent - prevSent;
            historyRecv.add(dRecv);
            historySent.add(dSent);
            prune(historyRecv, 1000);
            prune(historySent, 1000);
            return Collections.singletonList(new NetworkMonitorReport(
                    dRecv,
                    dSent,
                    historyRecv,
                    historySent,
                    config.getMonitorTime()));
        }
        prevRecv = recv;
        prevSent = sent;
        return Collections.emptyList();
    }

    private void prune(LinkedList<Long> list, int length) {
        while (list.size() > length) {
            list.pop();
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.network;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }
}
