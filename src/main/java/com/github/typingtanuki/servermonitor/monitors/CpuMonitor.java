package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.core.History;
import com.github.typingtanuki.servermonitor.report.CpuMonitorReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Monitors CPU usage against a max allowed percentage
 */
public class CpuMonitor implements Monitor {
    private MainConfig config;
    private History history = new History(100);
    private Map<Integer, Double> prevTop = new LinkedHashMap<>();

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
                config.getCpu().getMaxUsage(),
                getTopProcesses(systemInfo.getOperatingSystem())));
    }

    private Map<String, String> getTopProcesses(OperatingSystem operatingSystem) {
        Map<String, String> top = new LinkedHashMap<>();
        OSProcess[] topCpu = operatingSystem.getProcesses(10, OperatingSystem.ProcessSort.CPU, false);
        Map<Integer, Double> newTop = new LinkedHashMap<>();
        for (OSProcess process : topCpu) {
            int pid = process.getProcessID();
            double cpu = process.calculateCpuPercent();
            newTop.put(pid, cpu);
            top.put(pid + " " + process.getName(), asPercent(cpu) + " " + compare(prevTop.get(pid), cpu));
        }
        prevTop = newTop;
        return top;
    }

    private String compare(Double prev, double cpu) {
        if (prev == null) {
            return "new";
        }
        if (prev - 0.01 < cpu && cpu < prev + 0.01) {
            return "-";
        }
        if (prev > cpu) {
            return "↓" + asPercent(prev - cpu);
        }
        return "↑" + asPercent(cpu - prev);
    }

    private String asPercent(double ratio) {
        return Math.round(ratio * 100) + "%";
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
