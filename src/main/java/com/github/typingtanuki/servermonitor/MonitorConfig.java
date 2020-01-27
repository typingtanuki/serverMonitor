package com.github.typingtanuki.servermonitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MonitorConfig {
    private long monitorTime = 30_000;
    private int maxUsage = 80;
    private List<String> processes = Collections.emptyList();

    public void from(List<String> lines) {
        for (String line : lines) {
            if (line.trim().startsWith("#")) {
                continue;
            }
            String key = line.split("=", 2)[0].trim();
            String value = line.split("=", 2)[1].trim();

            MonitorConfigKeys configKey = MonitorConfigKeys.valueOf(key);
            update(configKey, value.split(","));
        }
    }

    private void update(MonitorConfigKeys configKey, String[] value) {
        switch (configKey) {
            case monitorTime:
                this.monitorTime = singleLong(value);
                break;
            case maxUsage:
                this.maxUsage = singleInt(value);
                break;
            case processes:
                this.processes = stringList(value);
                break;
        }
    }

    private List<String> stringList(String[] value) {
        return Arrays.asList(value);
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

    public long monitorTime() {
        return monitorTime;
    }

    public int maxUsage() {
        return maxUsage;
    }

    public List<String> processes() {
        return new ArrayList<>(processes);
    }
}
