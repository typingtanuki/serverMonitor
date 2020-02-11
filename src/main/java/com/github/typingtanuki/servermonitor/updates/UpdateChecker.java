package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.monitors.Monitor;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public abstract class UpdateChecker implements Monitor {
    private static final Logger logger = LoggerFactory.getLogger(UpdateChecker.class);
    private final MonitorConfig config;

    public UpdateChecker(MonitorConfig config) {
        super();

        this.config = config;
    }

    public static UpdateChecker bestChecker(MonitorConfig config) {
        logger.info("Looking for best update checker");
        // Ordered list of checkers
        List<UpdateChecker> possibleCheckers = new LinkedList<>();
        possibleCheckers.add(new YumChecker(config));
        // Prefer apt-get over apt as the API is more stable
        possibleCheckers.add(new AptGetChecker(config));
        possibleCheckers.add(new AptChecker(config));

        for (UpdateChecker updateChecker : possibleCheckers) {
            if (updateChecker.isAvailable()) {
                return updateChecker;
            }
        }

        logger.warn("Could not find suitable update checker, disabling");
        return new NoUpdateChecker();
    }

    @Override
    public MonitorType getType() {
        return MonitorType.update;
    }

    @Override
    public boolean isEnabled() {
        return config.isCheckUpdates();
    }

    protected MonitorConfig getConfig() {
        return config;
    }

    protected abstract boolean isAvailable();
}
