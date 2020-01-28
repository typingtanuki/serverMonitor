package com.github.typingtanuki.servermonitor.connectors;

import com.github.typingtanuki.servermonitor.report.MonitorReport;

import java.util.List;

public interface Connector {
    void reportFailure(List<MonitorReport> failedMonitorReports);
}
