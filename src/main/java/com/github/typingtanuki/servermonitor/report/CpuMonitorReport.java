package com.github.typingtanuki.servermonitor.report;

import java.util.LinkedHashMap;
import java.util.Map;

public class CpuMonitorReport extends AbstractPercentMonitorReport {
    public CpuMonitorReport(long load, int maxUsage) {
        super(load, maxUsage);
    }

    @Override
    public String title() {
        return "CPU usage is too high";
    }

    @Override
    public String shortDescription() {
        return "CPU usage " + usage + "% (Maximum allowed " + maxUsage + "%)";
    }

    @Override
    public Map<String, Object> details() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Current Usage", usage + "%");
        out.put("Maximum Usage", maxUsage + "%");
        return out;
    }
}
