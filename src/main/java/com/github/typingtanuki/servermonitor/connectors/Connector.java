package com.github.typingtanuki.servermonitor.connectors;

import com.github.typingtanuki.servermonitor.report.MonitorReport;

public interface Connector {
    void reportFailure(MonitorReport failedMonitorReport);
}
