package com.github.typingtanuki.servermonitor.report;

import java.util.LinkedHashMap;
import java.util.Map;

public class PingMonitorReport extends AbstractBoolMonitorReport {
    public PingMonitorReport(String monitored) {
        super(monitored);
    }

    @Override
    public String getTitle() {
        return "ICMP ping test";
    }

    @Override
    public String getDescription() {
        return "Ping to " + monitored;
    }

    @Override
    public Map<String, Object> getDetails() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("server", monitored);
        return out;
    }
}
