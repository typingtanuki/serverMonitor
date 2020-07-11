package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.core.History;
import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import com.github.typingtanuki.servermonitor.monitors.ProcessInfo;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.bytesToHuman;
import static com.github.typingtanuki.servermonitor.report.ReportUtils.millisToHuman;

public class ProcessMonitorReport extends AbstractBoolMonitorReport {
    private long uptime;
    private int pid;
    private String name;
    private String commandLine;
    private History cpuHistory;
    private History memoryHistory;
    private long currentCpu;
    private long currentMemory;
    private long memoryPercent;
    private boolean running;

    @Deprecated
    public ProcessMonitorReport() {
        super();
    }

    public ProcessMonitorReport(String monitored) {
        super(monitored);
    }

    @Override
    public String getTitle() {
        return "Process " + monitored + " check";
    }

    @Override
    public String getDescription() {
        return "Running process check " + monitored;
    }

    @Override
    public Map<DetailKey, Object> getDetails() {
        Map<DetailKey, Object> out = new LinkedHashMap<>();
        out.put(DetailKey.PROCESS, monitored);

        if (cpuHistory != null) {
            out.put(DetailKey.CPU_HISTORY, cpuHistory);
        }
        if (memoryHistory != null) {
            out.put(DetailKey.MEMORY_HISTORY, memoryHistory);
        }

        if (running) {
            out.put(DetailKey.PID, pid);
            out.put(DetailKey.UPTIME, millisToHuman(uptime));
            out.put(DetailKey.NAME, name);
            out.put(DetailKey.COMMAND_LINE, commandLine);
            out.put(DetailKey.CURRENT_CPU, currentCpu + "%");
            out.put(DetailKey.CURRENT_MEMORY,
                    bytesToHuman(currentMemory) + " (" + memoryPercent + "%)");
        }

        return out;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.process;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }

    public void ok(ProcessInfo processInfo) {
        super.ok();
        this.running = processInfo.isRunning();
        this.pid = processInfo.getPid();
        this.uptime = processInfo.getUptime();
        this.name = processInfo.getName();
        this.commandLine = processInfo.getCommandLine();
        this.cpuHistory = processInfo.getCpuHistory();
        this.memoryHistory = processInfo.getMemoryHistory();
        this.currentCpu = processInfo.getCurrentCpu();
        this.currentMemory = processInfo.getCurrentMemory();
        this.memoryPercent = processInfo.getMemoryPercent();
    }

    public void ng(ProcessInfo processInfo) {
        super.ng();
        this.running = false;
        this.pid = -1;
        this.uptime = 0;
        this.name = processInfo.getName();
        this.commandLine = null;
        this.cpuHistory = processInfo.getCpuHistory();
        this.memoryHistory = processInfo.getMemoryHistory();
        this.currentCpu = 0;
        this.currentMemory = 0;
        this.memoryPercent = 0;
    }
}
