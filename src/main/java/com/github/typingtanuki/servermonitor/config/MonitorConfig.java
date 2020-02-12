package com.github.typingtanuki.servermonitor.config;

public abstract class MonitorConfig {
    private boolean enabled = true;

    public MonitorConfig() {
        super();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract void validate();

    protected void innerCopyTo(MonitorConfig targetConfig) {
        targetConfig.enabled = enabled;
    }
}
