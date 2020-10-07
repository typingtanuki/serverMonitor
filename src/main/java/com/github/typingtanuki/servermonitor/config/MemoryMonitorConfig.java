package com.github.typingtanuki.servermonitor.config;

public class MemoryMonitorConfig extends MonitorConfig {
   private int maxUsage = 80;
   private int historySize = 1000;

   public MemoryMonitorConfig() {
      super();
   }

   @Override
   public void validate() {
      if (maxUsage < 1 || maxUsage > 99) {
         throw new IllegalStateException("Memory usage should be between 1 and 99%");
      }
      if (historySize < 0) {
         throw new IllegalStateException(
               "Memory history size should be greater or equal to 0");
      }
   }

   public void copyTo(MemoryMonitorConfig targetConfig) {
      innerCopyTo(targetConfig);
      targetConfig.maxUsage = maxUsage;
      targetConfig.historySize = historySize;
   }

   public int getMaxUsage() {
      return maxUsage;
   }

   public void setMaxUsage(int maxUsage) {
      this.maxUsage = maxUsage;
   }

   public int getHistorySize() {
      return historySize;
   }

   public void setHistorySize(int historySize) {
      this.historySize = historySize;
   }
}
