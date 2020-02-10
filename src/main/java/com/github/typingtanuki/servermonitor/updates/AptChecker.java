package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Experimental, APT CLI is still in beta
 */
public class AptChecker extends LinuxUpdateChecker {
    private static final Pattern APT_LINE = Pattern.compile("([^/]+)/[^\\s]+\\s+([^\\s]+)\\s+.*");

    public AptChecker(MonitorConfig config) {
        super(config);
    }

    @Override
    protected String binaryName() {
        return "apt";
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<String> out = runAndReadOutput("apt", "list", "--upgradable");

        if (out == null) {
            return Collections.singletonList(new InvalidReport());
        }

        Map<String, String> updates = new LinkedHashMap<>();
        for (String line : out) {
            Matcher matcher = APT_LINE.matcher(line);
            if (matcher.matches()) {
                updates.put(matcher.group(1), matcher.group(2));
            }
        }

        return Collections.singletonList(new UpdateReport(updates));
    }
}
