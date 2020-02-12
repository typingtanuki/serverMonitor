package com.github.typingtanuki.servermonitor.config;

public class UpdateMonitorConfig extends MonitorConfig {
    @Override
    public void validate() {
        // Nothing to do
    }

    public void copyTo(UpdateMonitorConfig targetConfig) {
        innerCopyTo(targetConfig);
    }
}
