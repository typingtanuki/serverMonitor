package com.github.typingtanuki.servermonitor.config;

/**
 * Settings for the network load monitors
 */
public class NetworkMonitorConfig extends MonitorConfig {
   /** The maximum amount of past entries to preserve in history */
   private int historySize = 1000;

   @Override
   public void validate() {
      if (historySize < 0) {
         throw new IllegalStateException("Network history should be greater or equal to" +
                                         " 0");
      }
   }

   public int getHistorySize() {
      return historySize;
   }

   public void setHistorySize(int historySize) {
      this.historySize = historySize;
   }

   public void copyTo(NetworkMonitorConfig network) {
      network.historySize = historySize;
   }
}
