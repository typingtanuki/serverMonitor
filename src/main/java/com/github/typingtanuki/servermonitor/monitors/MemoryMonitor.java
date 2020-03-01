package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.core.History;
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
public class MemoryMonitor implements Monitor {
    private MainConfig config;
    private History history = new History(100);

    public MemoryMonitor(MainConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        GlobalMemory memory = systemInfo.getHardware().getMemory();

        long free = memory.getAvailable();
        long total = memory.getTotal();
        history.touch(usage(free, total), config.getMemory().getHistorySize(), (long) config.getMemory().getMaxUsage());

        if (total <= 0) {
            return Collections.singletonList(new InvalidReport(getType(), getCategory()));
        }

        return Collections.singletonList(new MemoryMonitorReport(
                free,
                total,
                history,
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
