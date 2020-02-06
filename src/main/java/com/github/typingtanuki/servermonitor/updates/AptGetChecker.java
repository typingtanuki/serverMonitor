package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Uses apt-get cached data to check for update.
 * <p>
 * Updating the cache itself requires admin priviledges
 */
public class AptGetChecker extends LinuxUpdateChecker {
    @Override
    protected String binaryName() {
        return "apt-get";
    }

    @Override
    public MonitorReport check() {
        ProcessBuilder builder = new ProcessBuilder("apt-get", "--just-print", "upgrade");
        List<String> out;
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

        List<String> updates = new LinkedList<>();
        for (String line : out) {
            if (line.contains("])")) {
                updates.add(line);
            }
        }
        return new UpdateReport(updates);
    }
}
