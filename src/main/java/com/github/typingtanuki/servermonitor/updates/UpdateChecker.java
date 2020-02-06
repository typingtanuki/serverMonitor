package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.report.MonitorReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public abstract class UpdateChecker {
    private static final Logger logger = LoggerFactory.getLogger(UpdateChecker.class);

    public static UpdateChecker bestChecker(boolean checkUpdates) {
        if (!checkUpdates) {
            logger.info("Update checker is disabled in config");
            return null;
        }

        logger.info("Looking for best update checker");
        // Ordered list of checkers
        List<UpdateChecker> possibleCheckers = new LinkedList<>();
        possibleCheckers.add(new YumChecker());
        // Prefer apt-get over apt as the API is more stable
        possibleCheckers.add(new AptGetChecker());
        possibleCheckers.add(new AptChecker());

        for (UpdateChecker updateChecker : possibleCheckers) {
            if (updateChecker.isAvailable()) {
                return updateChecker;
            }
        }

        logger.warn("Could not find suitable update checker, disabling");
        return null;
    }

    protected abstract boolean isAvailable();

    public abstract MonitorReport check();
}
