package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.Collections;
import java.util.Map;

public class InvalidReport implements MonitorReport {
   private MonitorCategory monitorCategory;
   private MonitorType monitorType;

   public InvalidReport(MonitorType monitorType,
                        MonitorCategory monitorCategory) {
      super();
      this.monitorType = monitorType;
      this.monitorCategory = monitorCategory;
   }

   @Override
   public boolean isOK() {
      return true;
   }

   @Override
   public String getTitle() {
      return "State is unknown";
   }

   @Override
   public String getDescription() {
      return "State is unknown";
   }

   @Override
   public Map<DetailKey, Object> getDetails() {
      return Collections.emptyMap();
   }

   @Override
   public MonitorType getType() {
      return monitorType;
   }

   @Override
   public MonitorCategory getCategory() {
      return monitorCategory;
   }

   @Override
   public String monitorKey() {
      return getType().name();
   }

}
