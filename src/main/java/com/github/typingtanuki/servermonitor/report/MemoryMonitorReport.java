package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.bytesToHuman;

public class MemoryMonitorReport extends AbstractPercentMonitorReport {
    private final List<Long> history;
    private final List<String> historyDates;

    public MemoryMonitorReport(long free, long total, List<Long> history, List<String> historyDates, int maxUsage) {
        super(free, total, maxUsage);
        this.history = history;
        this.historyDates = historyDates;
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
        out.put("History", history);
        out.put("History Dates", historyDates);
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
