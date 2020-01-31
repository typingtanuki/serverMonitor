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
    public String title() {
        return "Disk usage on " + monitored + " is too high";
    }

    @Override
    public String shortDescription() {
        return "Disk usage " + monitored + " - " + usage + "% (Maximum allowed " + maxUsage + "%) Free: " + free + " Total: " + total;
    }

    @Override
    public Map<String, Object> details() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Disk", monitored);
        out.put("Current Usage", usage + "%");
        out.put("Maximum Usage", maxUsage + "%");
        out.put("Free space", bytesToHuman(free));
        out.put("Total space", bytesToHuman(total));
        return out;
    }

}
