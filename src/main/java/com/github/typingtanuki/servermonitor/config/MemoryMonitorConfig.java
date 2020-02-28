package com.github.typingtanuki.servermonitor.config;

public class MemoryMonitorConfig extends MonitorConfig {
    private int maxUsage = 80;
    private int historySize = 1000;

    public MemoryMonitorConfig() {
        super();
    }

    @Override
    public void validate() {
        if (maxUsage < 1 || maxUsage > 99) {
            throw new IllegalStateException("Memory usage should be between 1 and 99%");
        }
    }

    public void copyTo(MemoryMonitorConfig targetConfig) {
        innerCopyTo(targetConfig);
        targetConfig.maxUsage = maxUsage;
    }

    public int getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(int maxUsage) {
        this.maxUsage = maxUsage;
    }

    public int getHistorySize() {
        return historySize;
    }

    public void setHistorySize(int historySize) {
        this.historySize = historySize;
    }
}
