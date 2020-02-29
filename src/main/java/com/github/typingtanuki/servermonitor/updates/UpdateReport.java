package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import com.github.typingtanuki.servermonitor.report.AbstractBoolMonitorReport;
import com.github.typingtanuki.servermonitor.report.DetailKey;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateReport extends AbstractBoolMonitorReport {
    private final Map<String, String> updates;

    public UpdateReport(Map<String, String> updates) {
        super("Updates");
        if (updates.isEmpty()) {
            ok();
        } else {
            ng();
        }
        this.updates = updates;
    }

    @Override
    public String getTitle() {
        return "System updates";
    }

    @Override
    public String getDescription() {
        return "There are " + updates.size() + " updates available.";
    }

    @Override
    public Map<DetailKey, Object> getDetails() {
        Map<DetailKey, Object> details = new LinkedHashMap<>();
            details.put(DetailKey.UPDATES, updates);
        return details;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.update;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.system;
    }
}
