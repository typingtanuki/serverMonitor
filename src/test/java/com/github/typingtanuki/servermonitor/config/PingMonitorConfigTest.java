package com.github.typingtanuki.servermonitor.config;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PingMonitorConfigTest {
   @Test
   public void defaultValid() {
      new PingMonitorConfig().validate();
   }

   @Test
   public void emptyMonitoringValid() {
      PingMonitorConfig config = new PingMonitorConfig();
      config.setMonitoring(Collections.emptyList());
      config.validate();
   }

   @Test
   public void multipleMonitoringValid() {
      PingMonitorConfig config = new PingMonitorConfig();
      List<String> monitoring = new ArrayList<>();
      monitoring.add("a");
      monitoring.add("b");
      config.setMonitoring(monitoring);
      config.validate();
   }

   @Test
   public void copyTo() {
      PingMonitorConfig config1 = new PingMonitorConfig();
      List<String> monitoring = new ArrayList<>();
      monitoring.add("a");
      monitoring.add("b");
      config1.setMonitoring(monitoring);
      PingMonitorConfig config2 = new PingMonitorConfig();
      config1.copyTo(config2);
      assertThat(config1.getMonitoring(), is(config2.getMonitoring()));
   }
}