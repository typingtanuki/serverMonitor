package com.github.typingtanuki.servermonitor.report;

public class PingMonitorReport extends AbstractBoolMonitorReport {
    public PingMonitorReport(String monitored) {
        super(monitored);
    }

    @Override
    public String shortDescription() {
        return "Ping to " + monitored;
    }
}
