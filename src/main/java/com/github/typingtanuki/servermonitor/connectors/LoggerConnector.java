package com.github.typingtanuki.servermonitor.connectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author clerc
 * @since 2020/02/04
 */
public class LoggerConnector implements Connector {
    private static final Logger logger = LoggerFactory.getLogger(LoggerConnector.class);
    private static final ObjectWriter writer = new ObjectMapper().writerFor(Map.class).withDefaultPrettyPrinter();

    @Override
    public void reportFailure(MonitorReport failedMonitorReport) {
        String details;
        try {
            details = writer.writeValueAsString(failedMonitorReport.details());
        } catch (IOException e) {
            details = "Failed to get details";
            logger.warn(details, e);
        }
        logger.warn("{} - {}\r\n{}",
                failedMonitorReport.title(),
                failedMonitorReport.shortDescription(),
                details);
    }
}
