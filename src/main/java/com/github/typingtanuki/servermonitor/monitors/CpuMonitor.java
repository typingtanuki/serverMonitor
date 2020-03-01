package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.core.History;
import com.github.typingtanuki.servermonitor.report.CpuMonitorReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.util.Collections;
import java.util.List;

/**
 * Monitors CPU usage against a max allowed percentage
 */
public class CpuMonitor implements Monitor {
    private MainConfig config;
    private History history = new History(100);

    public CpuMonitor(MainConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        double[] loads = processor.getSystemLoadAverage(1);
        long last = (long) loads[0];
        history.touch(last, config.getCpu().getHistorySize(), (long) config.getCpu().getMaxUsage());
        return Collections.singletonList(new CpuMonitorReport(
                last,
                history,
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
