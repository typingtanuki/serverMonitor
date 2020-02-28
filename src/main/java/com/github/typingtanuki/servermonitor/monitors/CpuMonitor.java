package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.CpuMonitorReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import org.glassfish.jersey.internal.guava.Lists;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Monitors CPU usage against a max allowed percentage
 */
public class CpuMonitor extends WithHistory implements Monitor {
    private MainConfig config;

    public CpuMonitor(MainConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        double[] loads = processor.getSystemLoadAverage(1);
        long last = (long) loads[0];
        touch(last, config.getCpu().getHistorySize());
        return Collections.singletonList(new CpuMonitorReport(
                last,
                getHistory(),
                getHistoryDate(),
                config.getCpu().getMaxUsage()));
    }

    @Override
    public boolean isEnabled() {
        return config.getCpu().isEnabled();
    }

    @Override
    public MonitorType getType() {
        return MonitorType.cpu;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }
}
