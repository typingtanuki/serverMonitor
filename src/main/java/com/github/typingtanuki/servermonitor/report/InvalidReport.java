package com.github.typingtanuki.servermonitor.report;

import java.util.Collections;
import java.util.Map;

public class InvalidReport implements MonitorReport {
    @Override
    public boolean isOK() {
        return true;
    }

    @Override
    public String title() {
        return "State is unknown";
    }

    @Override
    public String shortDescription() {
        return "State is unknown";
    }

    @Override
    public Map<String, Object> details() {
        return Collections.emptyMap();
    }

}
