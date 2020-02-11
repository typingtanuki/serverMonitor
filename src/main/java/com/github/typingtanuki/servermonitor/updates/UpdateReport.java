package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import com.github.typingtanuki.servermonitor.report.AbstractBoolMonitorReport;

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
        return "System updates available";
    }

    @Override
    public String getDescription() {
        return "There are " + updates.size() + " updates available.";
    }

    @Override
    public Map<String, Object> getDetails() {
        Map<String, Object> details = new LinkedHashMap<>();
        for (Map.Entry<String, String> update : updates.entrySet()) {
            details.put(update.getKey(), update.getValue());
        }
        return details;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.update;
    }
}
