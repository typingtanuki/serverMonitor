package com.github.typingtanuki.servermonitor.report;

import java.util.Collections;
import java.util.Map;

public class InvalidReport implements MonitorReport {
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

}
