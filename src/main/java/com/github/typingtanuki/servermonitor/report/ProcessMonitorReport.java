package com.github.typingtanuki.servermonitor.report;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProcessMonitorReport extends AbstractBoolMonitorReport {
    public ProcessMonitorReport(String monitored) {
        super(monitored);
    }

    @Override
    public String title() {
        return "Process " + monitored + " is not running";
    }

    @Override
    public String shortDescription() {
        return "Running process check " + monitored;
    }

    @Override
    public Map<String, Object> details() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Process", monitored);
        return out;
    }
}
