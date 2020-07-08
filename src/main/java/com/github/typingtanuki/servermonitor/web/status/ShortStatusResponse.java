package com.github.typingtanuki.servermonitor.web.status;

import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.Status;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShortStatusResponse {
   private String identity;
   private Map<MonitorType, Boolean> status = new LinkedHashMap<>();

   public ShortStatusResponse() {
      super();
   }

   public ShortStatusResponse(String identity, Status current) {
      super();

      this.identity = identity;
      for (MonitorReport report : current.getSuccess()) {
         status.put(report.getType(), true);
      }
      for (MonitorReport report : current.getFailure()) {
         status.put(report.getType(), false);
      }
   }

   public String getIdentity() {
      return identity;
   }

   public void setIdentity(String identity) {
      this.identity = identity;
   }

   public Map<MonitorType, Boolean> getStatus() {
      return status;
   }

   public void setStatus(Map<MonitorType, Boolean> status) {
      this.status = status;
   }
}
