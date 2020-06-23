package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.ProcessMonitorReport;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Monitors running processes on the server.
 * <p>
 * If an expected process is not running, a failure will be raised
 */
public class ProcessMonitor implements Monitor {
    private MainConfig config;

    public ProcessMonitor(MainConfig config) {
        super();

        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<String> processes = new ArrayList<>(config.getProcess().getMonitoring());

        List<OSProcess> current = systemInfo.getOperatingSystem().getProcesses(0, OperatingSystem.ProcessSort.PID);
        List<MonitorReport> out = new LinkedList<>();
        int pid = -1;
        long uptime = -1;
        String name = null;
        String commandLine = null;
        for (String proc : processes) {
            ProcessMonitorReport report = new ProcessMonitorReport(proc);
            boolean running = false;
            for (OSProcess c : current) {
                if (c.getName().contains(proc) || c.getCommandLine().contains(proc)) {
                    running = true;
                    pid = c.getProcessID();
                    uptime = c.getUpTime();
                    name = c.getName();
                    commandLine = c.getCommandLine();
                    break;
                }
            }
            if (running) {
                report.ok(pid, uptime, name, commandLine);
            } else {
                report.ng();
            }
            out.add(report);
        }
        return out;
    }

    @Override
    public boolean isEnabled() {
        if (!config.getProcess().isEnabled()) {
            return false;
        }
        List<String> monitoring = config.getProcess().getMonitoring();
        return monitoring != null && !monitoring.isEmpty();
    }

    @Override
    public MonitorType getType() {
        return MonitorType.process;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }
}
