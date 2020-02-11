package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.Collections;
import java.util.Map;

public class InvalidReport implements MonitorReport {
    private MonitorType monitorType;

    public InvalidReport(MonitorType monitorType) {
        super();
        this.monitorType = monitorType;
    }

    @Override
    public boolean isOK() {
        return true;
    }

    @Override
    public String getTitle() {
        return "State is unknown";
    }

    @Override
    public String getDescription() {
        return "State is unknown";
    }

    @Override
    public Map<String, Object> getDetails() {
        return Collections.emptyMap();
    }

    @Override
    public MonitorType getType() {
        return monitorType;
    }

}
