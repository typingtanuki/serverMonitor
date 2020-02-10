package com.github.typingtanuki.servermonitor.report;

import java.util.LinkedHashMap;
import java.util.Map;

public class CpuMonitorReport extends AbstractPercentMonitorReport {
    public CpuMonitorReport(long load, int maxUsage) {
        super(load, maxUsage);
    }

    @Override
    public String getTitle() {
        return "CPU usage";
    }

    @Override
    public String getDescription() {
        return "CPU usage " + usage + "% (Maximum allowed " + maxUsage + "%)";
    }

    @Override
    public Map<String, Object> getDetails() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Current Usage", usage + "%");
        out.put("Maximum Usage", maxUsage + "%");
        return out;
    }
}
