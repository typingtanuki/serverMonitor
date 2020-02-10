package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;

import java.util.Collections;
import java.util.List;

public class NoUpdateChecker extends UpdateChecker {
    public NoUpdateChecker() {
        super(null);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    protected boolean isAvailable() {
        return false;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        return Collections.emptyList();
    }
}
