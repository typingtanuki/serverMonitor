package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.bytesToHuman;

public class MemoryMonitorReport extends AbstractPercentMonitorReport {
    public MemoryMonitorReport(long free, long total, int maxUsage) {
        super(free, total, maxUsage);
    }

    @Override
    public String getTitle() {
        return "Memory usage";
    }

    @Override
    public String getDescription() {
        return "Memory usage " + usage + "% (" +
                "Maximum allowed " + maxUsage + "%) " +
                "Free: " + bytesToHuman(free) + " " +
                "Total: " + bytesToHuman(total);
    }

    @Override
    public Map<String, Object> getDetails() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Current Usage", usage + "%");
        out.put("Maximum Usage", maxUsage + "%");
        out.put("Free memory", bytesToHuman(free));
        out.put("Total memory", bytesToHuman(total));
        return out;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.memory;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }
}
