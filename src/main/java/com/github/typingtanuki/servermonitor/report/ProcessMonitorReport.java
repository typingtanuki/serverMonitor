package com.github.typingtanuki.servermonitor.report;

public class ProcessMonitorReport extends AbstractBoolMonitorReport {
    public ProcessMonitorReport(String monitored) {
        super(monitored);
    }

    @Override
    public String shortDescription() {
        return "Running process check " + monitored;
    }
}
