package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CpuMonitorReport extends AbstractPercentMonitorReport {
    private final List<Long> history;
    private final List<String> historyDates;

    public CpuMonitorReport(long load,
                            List<Long> history,
                            List<String> historyDates,
                            int maxUsage) {
        super(load, maxUsage);
        this.history = history;
        this.historyDates = historyDates;
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
        out.put("History", history);
        out.put("History Dates", historyDates);
        return out;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.cpu;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }
}
