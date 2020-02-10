package com.github.typingtanuki.servermonitor.report;

import java.util.Map;

public interface MonitorReport {
    boolean isOK();

    String getTitle();

    String getDescription();

    Map<String, Object> getDetails();
}
