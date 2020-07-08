package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.now;

public class PingMonitorReport extends AbstractBoolMonitorReport {
   private String method;
   private String cause;
   private String lastSeen;

   public PingMonitorReport(String monitored) {
      super(monitored);
   }

   @Override
   public String getTitle() {
      return "Ping test";
   }

   @Override
   public String getDescription() {
      return "Ping to " + monitored;
   }

   @Override
   public Map<DetailKey, Object> getDetails() {
      Map<DetailKey, Object> out = new LinkedHashMap<>();
      out.put(DetailKey.SERVER, monitored);
      if (cause != null) {
         out.put(DetailKey.CAUSE, cause);
         out.put(DetailKey.LAST_SEEN, lastSeen);
      } else {
         out.put(DetailKey.METHOD, method);
      }
      return out;
   }

   @Override
   public MonitorType getType() {
      return MonitorType.ping;
   }

   @Override
   public MonitorCategory getCategory() {
      return MonitorCategory.remote;
   }

   public String getMethod() {
      return method;
   }

   public void setMethod(String method) {
      this.method = method;
   }

   public String getCause() {
      return cause;
   }

   public void setCause(String cause) {
      this.cause = cause;
   }

   public String ok(String method) {
      this.method = method;
      this.cause = null;
      super.ok();
      return now();
   }

   public void ng(Exception cause, String lastSeen) {
      this.cause = cause.getMessage();
      this.lastSeen = lastSeen;
      super.ng();
   }

}
