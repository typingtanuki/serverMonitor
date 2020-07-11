package com.github.typingtanuki.servermonitor.report;

public abstract class AbstractPercentMonitorReport implements MonitorReport {
    protected long free;
    protected long total;
    protected int maxUsage;
    protected long usage;

    @Deprecated
    public AbstractPercentMonitorReport() {
        super();
    }

    public AbstractPercentMonitorReport(long free, long total, int maxUsage) {
        super();

        this.free = free;
        this.total = total;
        this.maxUsage = maxUsage;

        this.usage = usage(free, total);
    }

    public AbstractPercentMonitorReport(long usage, int maxUsage) {
        super();

        this.free = -1;
        this.total = -1;

        this.usage = usage;
        this.maxUsage = maxUsage;
    }

    public static long usage(long free, long total) {
        return 100 - free * 100 / total;
    }

    @Override
    public boolean isOK() {
        return usage < maxUsage;
    }

    @Override
    public String monitorKey() {
        return getType().name();
    }
}
