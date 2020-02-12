package com.github.typingtanuki.servermonitor.config;

public class DiskMonitorConfig extends MonitorConfig {
    private int maxUsage = 80;

    public DiskMonitorConfig() {
        super();
    }

    @Override
    public void validate() {
        if (maxUsage < 1 || maxUsage > 99) {
            throw new IllegalStateException("Disk usage should be between 1 and 99%");
        }
    }

    public void copyTo(DiskMonitorConfig targetConfig) {
        innerCopyTo(targetConfig);
        targetConfig.maxUsage = maxUsage;
    }

    public int getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(int maxUsage) {
        this.maxUsage = maxUsage;
    }
}
