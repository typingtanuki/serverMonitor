package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
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
    private MonitorConfig config;

    public MemoryMonitor(MonitorConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        GlobalMemory memory = systemInfo.getHardware().getMemory();

        long free = memory.getAvailable();
        long total = memory.getTotal();
        if (total <= 0) {
            return Collections.singletonList(new InvalidReport());
        }

        return Collections.singletonList(new MemoryMonitorReport(free, total, config.maxMemoryUsage()));
    }
}
