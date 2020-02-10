package com.github.typingtanuki.servermonitor.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonitorConfig {
    private long monitorTime = 30_000;
    private int maxCpuUsage = 80;
    private int maxDiskUsage = 80;
    private int maxMemoryUsage = 80;
    private List<String> processes = Collections.emptyList();
    private List<String> ping = Collections.emptyList();
    private String identity = null;
    private String teamsHook = null;
    private int handshakePort = 9191;
    private List<String> handshake;
    private int maxHandshakeTime = 2_000;
    private boolean checkUpdates = true;


    public void copyTo(MonitorConfig config) {
        config.monitorTime = monitorTime;
        config.maxCpuUsage = maxCpuUsage;
        config.maxDiskUsage = maxDiskUsage;
        config.maxMemoryUsage = maxMemoryUsage;
        config.processes = processes;
        config.ping = ping;
        config.identity = identity;
        config.teamsHook = teamsHook;
        config.identity = identity;
        config.handshakePort = handshakePort;
        config.handshake = handshake;
        config.maxHandshakeTime = maxHandshakeTime;
        config.checkUpdates = checkUpdates;
    }

    public long getMonitorTime() {
        return monitorTime;
    }

    public void setMonitorTime(long monitorTime) {
        this.monitorTime = monitorTime;
    }

    public int getMaxCpuUsage() {
        return maxCpuUsage;
    }

    public void setMaxCpuUsage(int maxCpuUsage) {
        this.maxCpuUsage = maxCpuUsage;
    }

    public int getMaxDiskUsage() {
        return maxDiskUsage;
    }

    public void setMaxDiskUsage(int maxDiskUsage) {
        this.maxDiskUsage = maxDiskUsage;
    }

    public int getMaxMemoryUsage() {
        return maxMemoryUsage;
    }

    public void setMaxMemoryUsage(int maxMemoryUsage) {
        this.maxMemoryUsage = maxMemoryUsage;
    }

    public List<String> getProcesses() {
        return new ArrayList<>(processes);
    }

    public void setProcesses(List<String> processes) {
        this.processes = processes;
    }

    public List<String> getPing() {
        return new ArrayList<>(ping);
    }

    public void setPing(List<String> ping) {
        this.ping = ping;
    }

    public void validate() {
        if (identity == null || identity.isBlank()) {
            throw new IllegalStateException("Missing identity in settings");
        }
        if (monitorTime < 1000 || monitorTime > 3_600_000) {
            throw new IllegalStateException("Monitor time should be between 1s and 1day");
        }

        if (maxCpuUsage != -1 && (maxCpuUsage < 1 || maxCpuUsage > 99)) {
            throw new IllegalStateException("CPU usage should be between 1 and 99%");
        }
        if (maxDiskUsage != -1 && (maxDiskUsage < 1 || maxDiskUsage > 99)) {
            throw new IllegalStateException("Disk usage should be between 1 and 99%");
        }
        if (maxMemoryUsage != -1 && (maxMemoryUsage < 1 || maxMemoryUsage > 99)) {
            throw new IllegalStateException("Memory usage should be between 1 and 99%");
        }
        if (handshakePort < 1 || handshakePort > 65535) {
            throw new IllegalStateException("Handshake port should be between 1 and 65535");
        }
        if (maxHandshakeTime < 1 || maxHandshakeTime > 60_000) {
            throw new IllegalStateException("Max handshake time should be between 1ms and 1min");
        }
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getTeamsHook() {
        return teamsHook;
    }

    public void setTeamsHook(String teamsHook) {
        this.teamsHook = teamsHook;
    }

    public List<String> getHandshake() {
        return new ArrayList<>(handshake);
    }

    public void setHandshake(List<String> handshake) {
        this.handshake = handshake;
    }

    public int getHandshakePort() {
        return handshakePort;
    }

    public void setHandshakePort(int handshakePort) {
        this.handshakePort = handshakePort;
    }

    public int getMaxHandshakeTime() {
        return maxHandshakeTime;
    }

    public void setMaxHandshakeTime(int maxHandshakeTime) {
        this.maxHandshakeTime = maxHandshakeTime;
    }

    public boolean isCheckUpdates() {
        return checkUpdates;
    }

    public void setCheckUpdates(boolean checkUpdates) {
        this.checkUpdates = checkUpdates;
    }
}
