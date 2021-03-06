package com.github.typingtanuki.servermonitor.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Settings for the process monitors
 */
public class ProcessMonitorConfig extends MonitorConfig {
   /** List of processes to monitor */
   private List<String> monitoring = Collections.emptyList();
   /** The maximum amount of past entries to preserve in history */
   private int historySize = 1000;


   public ProcessMonitorConfig() {
      super();
   }

   @Override
   public void validate() {
      if (historySize < 0) {
         throw new IllegalStateException(
               "Process CPU history should be greater or equal to 0");
      }
   }

   public void copyTo(ProcessMonitorConfig targetConfig) {
      innerCopyTo(targetConfig);
      targetConfig.monitoring = new ArrayList<>(monitoring);
      targetConfig.historySize = historySize;
   }

   public List<String> getMonitoring() {
      return new ArrayList<>(monitoring);
   }

   public void setMonitoring(List<String> monitoring) {
      this.monitoring = new ArrayList<>(monitoring);
   }

   public int getHistorySize() {
      return historySize;
   }

   public void setHistorySize(int historySize) {
      this.historySize = historySize;
   }
}
