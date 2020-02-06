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

    public void from(List<String> lines) {
        String soFar = null;
        for (String line : lines) {
            if (line.trim().startsWith("#")) {
                continue;
            }
            if (soFar == null) {
                soFar = line;
            } else {
                soFar = soFar + line;
            }
            if (soFar.endsWith("\\")) {
                soFar = soFar.substring(0, soFar.length() - 1);
                continue;
            }
            String key = soFar.split("=", 2)[0].trim();
            String value = soFar.split("=", 2)[1].trim();

            MonitorConfigKeys configKey = MonitorConfigKeys.valueOf(key);
            update(configKey, value.split(","));
            soFar = null;
        }
        if (soFar != null) {
            throw new IllegalArgumentException("Last line finishes with \\");
        }
    }

    private void update(MonitorConfigKeys configKey, String[] value) {
        switch (configKey) {
            case monitorTime:
                this.monitorTime = singleLong(value);
                break;
            case maxCpuUsage:
                this.maxCpuUsage = singleInt(value);
                break;
            case maxDiskUsage:
                this.maxDiskUsage = singleInt(value);
                break;
            case maxMemoryUsage:
                this.maxMemoryUsage = singleInt(value);
                break;
            case processes:
                this.processes = stringList(value);
                break;
            case ping:
                this.ping = stringList(value);
                break;
            case identity:
                this.identity = singleString(value);
                break;
            case teamsHook:
                this.teamsHook = singleString(value);
                break;
            case handshake:
                this.handshake = stringList(value);
                break;
            case handshakePort:
                this.handshakePort = singleInt(value);
                break;
            case maxHandshakeTime:
                this.maxHandshakeTime = singleInt(value);
                break;
            case checkUpdates:
                this.checkUpdates = singleBoolean(value);
                break;
        }
    }

    private List<String> stringList(String[] values) {
        List<String> out = new ArrayList<>(values.length);
        for (String value : values) {
            out.add(value.trim());
        }
        return out;
    }

    private long singleLong(String[] value) {
        if (value.length != 1) {
            throw new IllegalArgumentException("Only a single number is possible, got " + String.join(", ", value));
        }
        return Long.parseLong(value[0]);
    }

    private int singleInt(String[] value) {
        if (value.length != 1) {
            throw new IllegalArgumentException("Only a single number is possible, got " + String.join(", ", value));
        }
        return Integer.parseInt(value[0]);
    }

    private boolean singleBoolean(String[] value) {
        if (value.length != 1) {
            throw new IllegalArgumentException("Only a single boolean is possible, got " + String.join(", ", value));
        }
        return Boolean.parseBoolean(value[0]);
    }

    private String singleString(String[] value) {
        if (value.length != 1) {
            throw new IllegalArgumentException("Only a single number is possible, got " + String.join(", ", value));
        }
        if (value[0] == null || value[0].isBlank()) {
            return null;
        }
        return value[0];
    }

    public long monitorTime() {
        return monitorTime;
    }

    public int maxCpuUsage() {
        return maxCpuUsage;
    }

    public int maxDiskUsage() {
        return maxDiskUsage;
    }

    public int maxMemoryUsage() {
        return maxMemoryUsage;
    }

    public List<String> processes() {
        return new ArrayList<>(processes);
    }

    public List<String> ping() {
        return new ArrayList<>(ping);
    }

    public void validate() {
        if (identity == null || identity.isBlank()) {
            throw new IllegalStateException("Missing identity in settings");
        }
        if (monitorTime < 1000 || monitorTime > 3_600_000) {
            throw new IllegalStateException("Monitor time should be between 1s and 1day");
        }

        if (maxCpuUsage < 1 || maxCpuUsage > 99) {
            throw new IllegalStateException("CPU usage should be between 1 and 99%");
        }
        if (maxDiskUsage < 1 || maxDiskUsage > 99) {
            throw new IllegalStateException("Disk usage should be between 1 and 99%");
        }
        if (maxMemoryUsage < 1 || maxMemoryUsage > 99) {
            throw new IllegalStateException("Memory usage should be between 1 and 99%");
        }
        if (handshakePort < 1 || handshakePort > 65535) {
            throw new IllegalStateException("Handshake port should be between 1 and 65535");
        }
        if (maxHandshakeTime < 1 || maxHandshakeTime > 60_000) {
            throw new IllegalStateException("Max handshake time should be between 1ms and 1min");
        }
    }

    public String identity() {
        return identity;
    }

    public String teamsHook() {
        return teamsHook;
    }

    public List<String> handshake() {
        return new ArrayList<>(handshake);
    }

    public int handshakePort() {
        return handshakePort;
    }

    public int maxHandshakeTime() {
        return maxHandshakeTime;
    }

    public boolean checkUpdates() {
        return checkUpdates;
    }
}
