package com.github.typingtanuki.servermonitor.config;

public class CpuMonitorConfig extends MonitorConfig {
    private int maxUsage = 80;

    public CpuMonitorConfig() {
        super();
    }

    @Override
    public void validate() {
        if (maxUsage < 1 || maxUsage > 99) {
            throw new IllegalStateException("CPU usage should be between 1 and 99%");
        }
    }

    public void copyTo(CpuMonitorConfig targetConfig) {
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
