package com.github.typingtanuki.servermonitor.report;

public class InvalidReport implements MonitorReport {
    @Override
    public boolean isOK() {
        return true;
    }

    @Override
    public String shortDescription() {
        return "State is unknown";
    }
}
