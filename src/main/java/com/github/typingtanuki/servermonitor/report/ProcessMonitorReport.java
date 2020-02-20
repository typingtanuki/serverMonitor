package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProcessMonitorReport extends AbstractBoolMonitorReport {
    private long uptime;
    private int pid;
    private String name;
    private String commandLine;

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
    public Map<String, Object> getDetails() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Process", monitored);
        if (pid >= 0) {
            out.put("PID", pid);
        }
        if (uptime >= 0) {
            out.put("Uptime", millisToHUman(uptime));
        }
        if (name != null) {
            out.put("Name", name);
        }
        if (commandLine != null) {
            out.put("Command Line", commandLine);
        }
        return out;
    }

    private String millisToHUman(long uptime) {
        if (uptime <= 1000) {
            return uptime + "ms";
        }
        if (uptime <= 60_000) {
            return (uptime / 1000) + "s";
        }
        if (uptime <= 3_600_000) {
            long inMin = uptime / 60_000;
            return inMin + "min " + millisToHUman(uptime - inMin * 60_000);
        }
        long inHour = uptime / 3_600_000;
        return inHour + "hour " + millisToHUman(uptime - inHour * 3_600_000);
    }

    @Override
    public MonitorType getType() {
        return MonitorType.process;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }

    public void ok(int pid, long uptime, String name, String commandLine) {
        super.ok();
        this.pid = pid;
        this.uptime = uptime;
        this.name = name;
        this.commandLine = commandLine;
    }
}
