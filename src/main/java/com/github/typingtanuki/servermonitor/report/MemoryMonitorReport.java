package com.github.typingtanuki.servermonitor.report;

public class MemoryMonitorReport extends AbstractPercentMonitorReport {
    public MemoryMonitorReport(long free, long total, int maxUsage) {
        super(free, total, maxUsage);
    }

    @Override
    public String shortDescription() {
        return "Memory usage " + usage + "% (Maximum allowed " + maxUsage + "%) Free: " + free + " Total: " + total;
    }
}
