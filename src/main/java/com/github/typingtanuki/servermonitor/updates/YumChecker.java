package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;

public class YumChecker extends LinuxUpdateChecker {
    @Override
    protected String binaryName() {
        return "yum";
    }

    @Override
    public MonitorReport check() {
        return new InvalidReport();
    }
}
