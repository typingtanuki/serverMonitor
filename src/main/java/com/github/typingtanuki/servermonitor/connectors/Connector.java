package com.github.typingtanuki.servermonitor.connectors;

import com.github.typingtanuki.servermonitor.report.MonitorReport;

/**
 * A way for failures to be reported to the outside world
 */
public interface Connector {
    /**
     * Send a report when there is a failure
     *
     * @param failedMonitorReport The failure to report
     */
    void reportFailure(MonitorReport failedMonitorReport);

    /**
     * Send a report when there is a recovery
     *
     * @param recoveredMonitorReport The succeeding report
     */
    void reportRecovery(MonitorReport recoveredMonitorReport);
}
