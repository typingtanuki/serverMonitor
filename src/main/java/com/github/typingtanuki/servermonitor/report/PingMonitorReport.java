package com.github.typingtanuki.servermonitor.report;

import java.util.LinkedHashMap;
import java.util.Map;

public class PingMonitorReport extends AbstractBoolMonitorReport {
    public PingMonitorReport(String monitored) {
        super(monitored);
    }

    @Override
    public String title() {
        return "ICMP ping test";
    }

    @Override
    public String shortDescription() {
        return "Ping to " + monitored;
    }

    @Override
    public Map<String, Object> details() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("server", monitored);
        return out;
    }
}
