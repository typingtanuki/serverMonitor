package com.github.typingtanuki.servermonitor.config;

/** Settings for the OS update monitor */
public class UpdateMonitorConfig extends MonitorConfig {
   @Override
   public void validate() {
      // Nothing to do
   }

   public void copyTo(UpdateMonitorConfig targetConfig) {
      innerCopyTo(targetConfig);
   }
}
