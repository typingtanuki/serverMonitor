package com.github.typingtanuki.servermonitor.config;

/**
 * Settings for the CPU monitors
 */
public class CpuMonitorConfig extends MonitorConfig {
   /** The usage (in percent) over which we are triggering a warning */
   private int maxUsage = 80;
   /** The maximum amount of past entries to preserve in history */
   private int historySize = 1000;

   public CpuMonitorConfig() {
      super();
   }

   @Override
   public void validate() {
      if (maxUsage < 1 || maxUsage > 99) {
         throw new IllegalStateException("CPU usage should be between 1 and 99%");
      }
      if (historySize < 0) {
         throw new IllegalStateException("CPU history should be greater or equal to 0");
      }
   }

   public void copyTo(CpuMonitorConfig targetConfig) {
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
