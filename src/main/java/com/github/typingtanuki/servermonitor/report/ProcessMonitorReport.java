package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.millisToHuman;

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
    public Map<DetailKey, Object> getDetails() {
        Map<DetailKey, Object> out = new LinkedHashMap<>();
        out.put(DetailKey.PROCESS, monitored);
        if (pid >= 0) {
            out.put(DetailKey.PID, pid);
        }
        if (uptime >= 0) {
            out.put(DetailKey.UPTIME, millisToHuman(uptime));
        }
        if (name != null) {
            out.put(DetailKey.NAME, name);
        }
        if (commandLine != null) {
            out.put(DetailKey.COMMAND_LINE, commandLine);
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

    public void ok(int pid, long uptime, String name, String commandLine) {
        super.ok();
        this.pid = pid;
        this.uptime = uptime;
        this.name = name;
        this.commandLine = commandLine;
    }
}
