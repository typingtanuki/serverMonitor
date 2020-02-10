package com.github.typingtanuki.servermonitor.report;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.bytesToHuman;

public class DiskMonitorReport extends AbstractPercentMonitorReport {
    private final String monitored;

    public DiskMonitorReport(String monitored, long free, long total, int maxUsage) {
        super(free, total, maxUsage);
        this.monitored = monitored;
    }

    @Override
    public String getTitle() {
        return "Disk usage on " + monitored;
    }

    @Override
    public String getDescription() {
        return "Disk usage " + monitored + " - " + usage + "% (Maximum allowed " + maxUsage + "%) Free: " + free + " Total: " + total;
    }

    @Override
    public Map<String, Object> getDetails() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Disk", monitored);
        out.put("Current Usage", usage + "%");
        out.put("Maximum Usage", maxUsage + "%");
        out.put("Free space", bytesToHuman(free));
        out.put("Total space", bytesToHuman(total));
        return out;
    }

}
