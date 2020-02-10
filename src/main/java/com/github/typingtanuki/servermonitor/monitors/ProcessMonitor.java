package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.ProcessMonitorReport;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Monitors running processes on the server.
 * <p>
 * If an expected process is not running, a failure will be raised
 */
public class ProcessMonitor implements Monitor {
    private MonitorConfig config;

    public ProcessMonitor(MonitorConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<String> processes = new ArrayList<>(config.getProcesses());

        OSProcess[] current = systemInfo.getOperatingSystem().getProcesses();
        List<MonitorReport> out = new LinkedList<>();
        for (String proc : processes) {
            ProcessMonitorReport report = new ProcessMonitorReport(proc);
            boolean running = false;
            for (OSProcess c : current) {
                if (c.getName().contains(proc)) {
                    running = true;
                    break;
                }
            }
            if (running) {
                report.ok();
            } else {
                report.ng();
            }
            out.add(report);
        }
        return out;
    }

    @Override
    public boolean isEnabled() {
        return config.getProcesses() != null && !config.getProcesses().isEmpty();
    }
}
