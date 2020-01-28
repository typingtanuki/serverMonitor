package com.github.typingtanuki.servermonitor.report;

public class CpuMonitorReport extends AbstractPercentMonitorReport {
    public CpuMonitorReport(long load, int maxUsage) {
        super(load, maxUsage);
    }

    @Override
    public String shortDescription() {
        return "CPU usage " + usage + "% (Maximum allowed " + maxUsage + "%)";
    }
}
