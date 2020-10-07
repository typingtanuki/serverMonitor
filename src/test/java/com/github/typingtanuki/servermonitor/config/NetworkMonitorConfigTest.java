package com.github.typingtanuki.servermonitor.config;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class NetworkMonitorConfigTest {
   @Test
   public void defaultValid() {
      new NetworkMonitorConfig().validate();
   }

   @Test
   public void invalidHistoryUnder() {
      NetworkMonitorConfig config = new NetworkMonitorConfig();
      config.setHistorySize(-10);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void validHistory() {
      NetworkMonitorConfig config = new NetworkMonitorConfig();
      config.setHistorySize(50);
      config.validate();
   }

   @Test
   public void copyTo() {
      NetworkMonitorConfig config1 = new NetworkMonitorConfig();
      config1.setHistorySize(234);
      NetworkMonitorConfig config2 = new NetworkMonitorConfig();
      config1.copyTo(config2);
      assertThat(config1.getHistorySize(), is(config2.getHistorySize()));
   }
}