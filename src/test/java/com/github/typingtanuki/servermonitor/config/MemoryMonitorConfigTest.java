package com.github.typingtanuki.servermonitor.config;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class MemoryMonitorConfigTest {
   @Test
   public void defaultValid() {
      new MemoryMonitorConfig().validate();
   }

   @Test
   public void invalidUsageUnder() {
      MemoryMonitorConfig config = new MemoryMonitorConfig();
      config.setMaxUsage(0);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidUsageOver() {
      MemoryMonitorConfig config = new MemoryMonitorConfig();
      config.setMaxUsage(100);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void validUsage() {
      MemoryMonitorConfig config = new MemoryMonitorConfig();
      config.setMaxUsage(50);
      config.validate();
   }

   @Test
   public void invalidHistoryUnder() {
      MemoryMonitorConfig config = new MemoryMonitorConfig();
      config.setHistorySize(-10);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void validHistory() {
      MemoryMonitorConfig config = new MemoryMonitorConfig();
      config.setHistorySize(50);
      config.validate();
   }

   @Test
   public void copyTo() {
      MemoryMonitorConfig config1 = new MemoryMonitorConfig();
      config1.setMaxUsage(12);
      config1.setHistorySize(234);
      MemoryMonitorConfig config2 = new MemoryMonitorConfig();
      config1.copyTo(config2);
      assertThat(config1.getMaxUsage(), is(config2.getMaxUsage()));
      assertThat(config1.getHistorySize(), is(config2.getHistorySize()));
   }

}