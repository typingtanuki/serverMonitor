package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;

import java.util.List;

public interface Monitor {
    List<MonitorReport> monitor(SystemInfo systemInfo);
}
