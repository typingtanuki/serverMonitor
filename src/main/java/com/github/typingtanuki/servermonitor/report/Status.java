package com.github.typingtanuki.servermonitor.report;

import java.util.ArrayList;
import java.util.List;

/**
 * The status of the latest update of the monitors
 *
 * @author clerc
 * @since 2020/02/07
 */
public class Status {
   private final List<MonitorReport> success = new ArrayList<>();
   private final List<MonitorReport> failure = new ArrayList<>();

   public Status() {
      super();
   }

   public Status(List<MonitorReport> reports) {
      super();

      for (MonitorReport report : reports) {
         if (report.isOK()) {
            success.add(report);
         } else {
            failure.add(report);
         }
      }
   }

   public List<MonitorReport> getSuccess() {
      return success;
   }

   public List<MonitorReport> getFailure() {
      return failure;
   }
}
