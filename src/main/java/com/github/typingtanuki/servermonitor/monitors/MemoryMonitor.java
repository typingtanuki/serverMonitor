package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MemoryMonitorReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.util.Collections;
import java.util.List;

import static com.github.typingtanuki.servermonitor.report.AbstractPercentMonitorReport.usage;

/**
 * Monitors Memory usage against a max allowed percentage
 */
public class MemoryMonitor extends WithHistory implements Monitor {
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
        touch(usage(free, total), config.getMemory().getHistorySize());

        if (total <= 0) {
            return Collections.singletonList(new InvalidReport(getType(), getCategory()));
        }

        return Collections.singletonList(new MemoryMonitorReport(
                free,
                total,
                getHistory(),
                getHistoryDate(),
                config.getMemory().getMaxUsage()));
    }

    @Override
    public boolean isEnabled() {
        return config.getMemory().isEnabled();
    }

    @Override
    public MonitorType getType() {
        return MonitorType.memory;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }
}
