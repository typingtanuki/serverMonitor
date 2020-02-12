package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YumChecker extends LinuxUpdateChecker {
    private static final Pattern YUM_LINE = Pattern.compile("^([^\\s]+)\\.[^\\s]+\\s+([^\\s]+)\\s+[^\\s]+$");

    public YumChecker(MainConfig config) {
        super(config);
    }

    @Override
    protected String binaryName() {
        return "yum";
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<String> out = runAndReadOutput("yum", "check-update");

        if (out == null) {
            return Collections.singletonList(new InvalidReport(getType()));
        }

        Map<String, String> updates = new LinkedHashMap<>();
        for (String line : out) {
            Matcher matcher = YUM_LINE.matcher(line);
            if (matcher.matches()) {
                updates.put(matcher.group(1), matcher.group(2));
            }
        }

        return Collections.singletonList(new UpdateReport(updates));
    }
}
