package com.github.typingtanuki.servermonitor.report;

public class DiskMonitorReport extends AbstractPercentMonitorReport {
    private final String monitored;

    public DiskMonitorReport(String monitored, long free, long total, int maxUsage) {
        super(free, total, maxUsage);
        this.monitored = monitored;
    }

    @Override
    public String shortDescription() {
        return "Disk usage " + monitored + " - " + usage + "% (Maximum allowed " + maxUsage + "%) Free: " + free + " Total: " + total;
    }
}
