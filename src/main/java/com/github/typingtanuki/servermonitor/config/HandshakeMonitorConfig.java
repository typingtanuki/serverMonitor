package com.github.typingtanuki.servermonitor.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Settings for the handshake monitors
 */
public class HandshakeMonitorConfig extends MonitorConfig {
   /** The list of servers we want to do an handshake with */
   private List<String> monitoring = Collections.emptyList();
   /** The time after we are giving up contacting the server and consider it "down" */
   private int maxHandshakeTime = 2_000;

   public HandshakeMonitorConfig() {
      super();
   }

   @Override
   public void validate() {
      if (maxHandshakeTime < 1 || maxHandshakeTime > 60_000) {
         throw new IllegalStateException(
               "Max handshake time should be between 1ms and 1min");
      }
   }

   public void copyTo(HandshakeMonitorConfig targetConfig) {
      innerCopyTo(targetConfig);
      targetConfig.monitoring = new ArrayList<>(monitoring);
      targetConfig.maxHandshakeTime = maxHandshakeTime;
   }

   public List<String> getMonitoring() {
      return new ArrayList<>(monitoring);
   }

   public void setMonitoring(List<String> monitoring) {
      this.monitoring = new ArrayList<>(monitoring);
   }

   public int getMaxHandshakeTime() {
      return maxHandshakeTime;
   }

   public void setMaxHandshakeTime(int maxHandshakeTime) {
      this.maxHandshakeTime = maxHandshakeTime;
   }
}
