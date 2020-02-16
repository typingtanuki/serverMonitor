package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProcessMonitorReport extends AbstractBoolMonitorReport {
    public ProcessMonitorReport(String monitored) {
        super(monitored);
    }

    @Override
    public String getTitle() {
        return "Process " + monitored + " check";
    }

    @Override
    public String getDescription() {
        return "Running process check " + monitored;
    }

    @Override
    public Map<String, Object> getDetails() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Process", monitored);
        return out;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.process;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }
}
