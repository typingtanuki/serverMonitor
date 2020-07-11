package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.bytesToHuman;

public class DiskMonitorReport extends AbstractPercentMonitorReport {
    private String monitored;

    @Deprecated
    public DiskMonitorReport() {
        super();
    }

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
        return "Disk usage " +
                monitored +
                " - " +
                usage +
                "% (Maximum allowed " +
                maxUsage +
                "%) Free: " +
                bytesToHuman(free) +
                " Total: " +
                bytesToHuman(total);
    }

    @Override
    public Map<DetailKey, Object> getDetails() {
        Map<DetailKey, Object> out = new LinkedHashMap<>();
        out.put(DetailKey.DISK, monitored);
        out.put(DetailKey.USAGE_CURRENT, usage + "%");
        out.put(DetailKey.USAGE_MAX, maxUsage + "%");
        out.put(DetailKey.SPACE_FREE, bytesToHuman(free));
        out.put(DetailKey.SPACE_TOTAL, bytesToHuman(total));
        return out;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.disk;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }

    @Override
    public String monitorKey() {
        return getType() + " " + monitored;
    }
}
