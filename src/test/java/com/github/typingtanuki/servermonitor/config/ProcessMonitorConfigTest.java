package com.github.typingtanuki.servermonitor.config;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class ProcessMonitorConfigTest {
   @Test
   public void defaultValid() {
      new ProcessMonitorConfig().validate();
   }

   @Test
   public void invalidHistoryUnder() {
      ProcessMonitorConfig config = new ProcessMonitorConfig();
      config.setHistorySize(-10);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void validHistory() {
      ProcessMonitorConfig config = new ProcessMonitorConfig();
      config.setHistorySize(50);
      config.validate();
   }


   @Test
   public void emptyMonitoringValid() {
      ProcessMonitorConfig config = new ProcessMonitorConfig();
      config.setMonitoring(Collections.emptyList());
      config.validate();
   }

   @Test
   public void multipleMonitoringValid() {
      ProcessMonitorConfig config = new ProcessMonitorConfig();
      List<String> monitoring = new ArrayList<>();
      monitoring.add("a");
      monitoring.add("b");
      config.setMonitoring(monitoring);
      config.validate();
   }

   @Test
   public void copyTo() {
      ProcessMonitorConfig config1 = new ProcessMonitorConfig();
      config1.setHistorySize(12);
      List<String> monitoring = new ArrayList<>();
      monitoring.add("a");
      monitoring.add("b");
      config1.setMonitoring(monitoring);
      ProcessMonitorConfig config2 = new ProcessMonitorConfig();
      config1.copyTo(config2);
      assertThat(config1.getHistorySize(), is(config2.getHistorySize()));
      assertThat(config1.getMonitoring(), is(config2.getMonitoring()));
   }
}