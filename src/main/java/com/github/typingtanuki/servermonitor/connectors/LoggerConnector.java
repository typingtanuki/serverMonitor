package com.github.typingtanuki.servermonitor.connectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.utils.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.utils.SimpleStack.simpleStack;

/**
 * A connector reporting through the local logger
 */
public class LoggerConnector implements Connector {
   private static final Logger logger = LoggerFactory.getLogger(LoggerConnector.class);

   @Override
   public void reportFailure(MonitorReport failedMonitorReport) {
      logger.warn("{} - {}\r\n{}",
                  failedMonitorReport.getTitle(),
                  failedMonitorReport.getDescription(),
                  details(failedMonitorReport));
   }

   @Override
   public void reportRecovery(MonitorReport recoveredMonitorReport) {
      logger.info("{} - {}\r\n{}",
                  recoveredMonitorReport.getTitle(),
                  recoveredMonitorReport.getDescription(),
                  details(recoveredMonitorReport));
   }

   private String details(MonitorReport recoveredMonitorReport) {
      try {
         return Json.pretty(recoveredMonitorReport.getDetails());
      } catch (IOException e) {
         String details = "Failed to get details";
         logger.warn(details + "\r\n{}", simpleStack(e));
         return details;
      }
   }
}
