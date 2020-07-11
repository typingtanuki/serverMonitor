package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.core.History;
import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.bytesToHuman;
import static com.github.typingtanuki.servermonitor.report.ReportUtils.millisToHuman;

public class NetworkMonitorReport implements MonitorReport {
    private long recv;
    private long sent;
    private History historyRecv;
    private History historySent;
    private long monitorTime;

    @Deprecated
    public NetworkMonitorReport() {
        super();
    }

    public NetworkMonitorReport(long recv,
                                long sent,
                                History historyRecv,
                                History historySent,
                                long monitorTime) {
        this.recv = recv;
        this.sent = sent;
        this.historyRecv = historyRecv;
        this.historySent = historySent;
        this.monitorTime = monitorTime;
    }

    @Override
    public boolean isOK() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Network Monitor";
    }

    @Override
    public String getDescription() {
        return "Traffic through all interfaces";
    }

    @Override
    public Map<DetailKey, Object> getDetails() {
        Map<DetailKey, Object> out = new LinkedHashMap<>();
        out.put(DetailKey.BYTES_RECEIVED, bytesToHuman(recv));
        out.put(DetailKey.BYTES_SENT, bytesToHuman(sent));
        out.put(DetailKey.INTERVAL, millisToHuman(monitorTime));
        out.put(DetailKey.HISTORY_RECEIVED, historyRecv);
        out.put(DetailKey.HISTORY_SENT, historySent);
        return out;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.network;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }

    @Override
    public String monitorKey() {
        return getType().name();
    }
}
