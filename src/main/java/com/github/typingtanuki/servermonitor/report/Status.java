package com.github.typingtanuki.servermonitor.report;

import java.util.LinkedList;
import java.util.List;

/**
 * @author clerc
 * @since 2020/02/07
 */
public class Status {
    private final List<MonitorReport> success = new LinkedList<>();
    private final List<MonitorReport> failure = new LinkedList<>();

    public Status() {
        super();
    }

    public Status(List<MonitorReport> reports) {
        super();

        for (MonitorReport report : reports) {
            if (report.isOK()) {
                success.add(report);
            } else {
                failure.add(report);
            }
        }
    }

    public List<MonitorReport> getSuccess() {
        return success;
    }

    public List<MonitorReport> getFailure() {
        return failure;
    }
}
