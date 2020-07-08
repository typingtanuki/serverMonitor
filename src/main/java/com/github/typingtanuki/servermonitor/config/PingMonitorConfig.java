package com.github.typingtanuki.servermonitor.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PingMonitorConfig extends MonitorConfig {
   private List<String> monitoring = Collections.emptyList();

   public PingMonitorConfig() {
      super();
   }

   @Override
   public void validate() {
      // Nothing to do
   }

   public void copyTo(PingMonitorConfig targetConfig) {
      innerCopyTo(targetConfig);
      targetConfig.monitoring = new ArrayList<>(monitoring);
   }

   public List<String> getMonitoring() {
      return new ArrayList<>(monitoring);
   }

   public void setMonitoring(List<String> monitoring) {
      this.monitoring = new ArrayList<>(monitoring);
   }
}
