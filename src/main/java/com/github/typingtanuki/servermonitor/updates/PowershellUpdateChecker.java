package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PowershellUpdateChecker extends WindowsUpdateChecker {
    public PowershellUpdateChecker(MainConfig config) {
        super(config);
    }

    @Override
    protected String binaryName() {
        return "powershell";
    }

    @Override
    public String runUpdate() {
        return "Can not handle updates on this platform";
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<String> out = runAndReadOutput(
                "cmd", "/c",
                "powershell",
                "-ExecutionPolicy RemoteSigned",
                "-noprofile -noninteractive",
                Paths.get("./script/update.ps1").toAbsolutePath().toString());

        if (out == null) {
            return Collections.singletonList(new InvalidReport(getType(), getCategory()));
        }

        Map<String, String> updates = new LinkedHashMap<>();
        for (String update : out) {
            updates.put(update, "");
        }

        return Collections.singletonList(new UpdateReport(updates));
    }
}
