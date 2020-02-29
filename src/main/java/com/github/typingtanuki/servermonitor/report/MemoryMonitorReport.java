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
    public Map<DetailKey, Object> getDetails() {
        Map<DetailKey, Object> out = new LinkedHashMap<>();
        out.put(DetailKey.USAGE_CURRENT, usage + "%");
        out.put(DetailKey.USAGE_MAX, maxUsage + "%");
        out.put(DetailKey.MEMORY_FREE, bytesToHuman(free));
        out.put(DetailKey.MEMORY_TOTAL, bytesToHuman(total));
        out.put(DetailKey.HISTORY, history);
        out.put(DetailKey.HISTORY_DATES, historyDates);
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
