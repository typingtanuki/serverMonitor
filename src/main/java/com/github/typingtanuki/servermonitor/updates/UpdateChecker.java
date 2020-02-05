package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.report.MonitorReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UpdateChecker {
    private static final Logger logger = LoggerFactory.getLogger(UpdateChecker.class);

    public static UpdateChecker bestChecker() {
        UpdateChecker out;

        out = new YumChecker().tryCheck();
        if (out != null) {
            return out;
        }
        out = new AptGetChecker().tryCheck();
        if (out != null) {
            return out;
        }
        out = new AptChecker().tryCheck();
        if (out != null) {
            return out;
        }

        logger.warn("Could not find suitable update checker, disabling");
        return null;
    }

    protected abstract UpdateChecker tryCheck();

    public abstract MonitorReport check();
}
