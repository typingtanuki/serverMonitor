package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;

import java.util.List;

/**
 * A tool running in single, point-in-time monitor on a single metrics
 */
public interface Monitor {
    /**
     * Run the monitor now
     *
     * @param systemInfo Information about the current system
     */
    List<MonitorReport> monitor(SystemInfo systemInfo);
}
