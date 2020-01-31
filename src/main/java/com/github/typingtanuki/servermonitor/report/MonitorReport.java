package com.github.typingtanuki.servermonitor.report;

import java.util.Map;

public interface MonitorReport {
    boolean isOK();

    String title();

    String shortDescription();

    Map<String, Object> details();
}
