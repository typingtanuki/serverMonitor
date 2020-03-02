package com.github.typingtanuki.servermonitor.config;

public class NetworkMonitorConfig extends MonitorConfig {
    private int historySize = 1000;

    @Override
    public void validate() {
        if (historySize < 0) {
            throw new IllegalStateException("Network history should be greater or equal to 0");
        }
    }

    public int getHistorySize() {
        return historySize;
    }

    public void setHistorySize(int historySize) {
        this.historySize = historySize;
    }

    public void copyTo(NetworkMonitorConfig network) {
        network.historySize = historySize;
    }
}
