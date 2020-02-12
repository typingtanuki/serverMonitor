package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MemoryMonitorReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.util.Collections;
import java.util.List;

/**
 * Monitors Memory usage against a max allowed percentage
 */
public class MemoryMonitor implements Monitor {
    private MainConfig config;

    public MemoryMonitor(MainConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        GlobalMemory memory = systemInfo.getHardware().getMemory();

        long free = memory.getAvailable();
        long total = memory.getTotal();
        if (total <= 0) {
            return Collections.singletonList(new InvalidReport(getType()));
        }

        return Collections.singletonList(new MemoryMonitorReport(free, total, config.getMemory().getMaxUsage()));
    }

    @Override
    public boolean isEnabled() {
        return config.getMemory().isEnabled();
    }

    @Override
    public MonitorType getType() {
        return MonitorType.memory;
    }
}
