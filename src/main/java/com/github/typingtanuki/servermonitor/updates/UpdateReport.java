package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.report.AbstractBoolMonitorReport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateReport extends AbstractBoolMonitorReport {
    private final List<String> updates;

    public UpdateReport(List<String> updates) {
        super("Updates");
        if (updates.isEmpty()) {
            ok();
        } else {
            ng();
        }
        this.updates = updates;
    }

    @Override
    public String title() {
        return "System updates available";
    }

    @Override
    public String shortDescription() {
        return "There are " + updates.size() + " updates available.";
    }

    @Override
    public Map<String, Object> details() {
        Map<String, Object> details = new LinkedHashMap<>();
        for (String update : updates) {
            details.put(update, "");
        }
        return details;
    }
}
