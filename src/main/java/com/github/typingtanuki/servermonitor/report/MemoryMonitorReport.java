package com.github.typingtanuki.servermonitor.report;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.bytesToHuman;

public class MemoryMonitorReport extends AbstractPercentMonitorReport {
    public MemoryMonitorReport(long free, long total, int maxUsage) {
        super(free, total, maxUsage);
    }

    @Override
    public String title() {
        return null;
    }

    @Override
    public String shortDescription() {
        return "Memory usage " + usage + "% (Maximum allowed " + maxUsage + "%) Free: " + free + " Total: " + total;
    }

    @Override
    public Map<String, Object> details() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Current Usage", usage + "%");
        out.put("Maximum Usage", maxUsage + "%");
        out.put("Free memory", bytesToHuman(free));
        out.put("Total memory", bytesToHuman(total));
        return out;
    }
}
