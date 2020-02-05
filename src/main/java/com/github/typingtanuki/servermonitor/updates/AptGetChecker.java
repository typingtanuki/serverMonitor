package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;

import java.io.IOException;

public class AptGetChecker extends LinuxUpdateChecker {
    @Override
    protected String binaryName() {
        return "apt-get";
    }

    @Override
    public MonitorReport check() {
        ProcessBuilder builder = new ProcessBuilder("apt-get", "--just-print", "upgrade");
        String out;
        try {
            out = runAndReadOutput(builder);
        } catch (IOException e) {
            logger.warn("Failed to get upgrade list", e);
            return new InvalidReport();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Interrupted while getting upgrade list", e);
            return new InvalidReport();
        }

        if (out.split("\\n", 2).length > 1) {
            logger.warn("Updates are waiting");
            return new InvalidReport();
        }
        logger.warn("No updates");
        return new InvalidReport();
    }
}
