package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.Map;

public interface MonitorReport {
    boolean isOK();

    String getTitle();

    String getDescription();

    Map<String, Object> getDetails();

    MonitorType getType();
}
