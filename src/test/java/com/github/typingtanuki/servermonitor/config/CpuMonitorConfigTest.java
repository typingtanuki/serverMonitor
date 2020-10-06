package com.github.typingtanuki.servermonitor.config;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class CpuMonitorConfigTest {
   @Test
   public void defaultValid() {
      new CpuMonitorConfig().validate();
   }

   @Test
   public void invalidUsageUnder() {
      CpuMonitorConfig config = new CpuMonitorConfig();
      config.setMaxUsage(0);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidUsageOver() {
      CpuMonitorConfig config = new CpuMonitorConfig();
      config.setMaxUsage(100);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void validUsage() {
      CpuMonitorConfig config = new CpuMonitorConfig();
      config.setMaxUsage(50);
      config.validate();
   }

   @Test
   public void invalidHistoryUnder() {
      CpuMonitorConfig config = new CpuMonitorConfig();
      config.setHistorySize(-10);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void validHistory() {
      CpuMonitorConfig config = new CpuMonitorConfig();
      config.setHistorySize(50);
      config.validate();
   }

   @Test
   public void copyTo() {
      CpuMonitorConfig config1 = new CpuMonitorConfig();
      config1.setMaxUsage(12);
      config1.setHistorySize(234);
      CpuMonitorConfig config2 = new CpuMonitorConfig();
      config1.copyTo(config2);
      assertThat(config1.getMaxUsage(), is(config2.getMaxUsage()));
      assertThat(config1.getHistorySize(), is(config2.getHistorySize()));
   }
}