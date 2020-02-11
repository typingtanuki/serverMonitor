package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
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
    private MonitorConfig config;

    public CpuMonitor(MonitorConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        double[] loads = processor.getSystemLoadAverage(1);
        return Collections.singletonList(new CpuMonitorReport((long) loads[0], config.getMaxCpuUsage()));
    }

    @Override
    public boolean isEnabled() {
        return config.getMaxCpuUsage() != -1;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.cpu;
    }
}
