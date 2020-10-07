package com.github.typingtanuki.servermonitor.config;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class HandshakeMonitorConfigTest {
   @Test
   public void defaultValid() {
      new HandshakeMonitorConfig().validate();
   }

   @Test
   public void invalidTimeUnder() {
      HandshakeMonitorConfig config = new HandshakeMonitorConfig();
      config.setMaxHandshakeTime(0);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidTimeOver() {
      HandshakeMonitorConfig config = new HandshakeMonitorConfig();
      config.setMaxHandshakeTime(60_001);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void validTime() {
      HandshakeMonitorConfig config = new HandshakeMonitorConfig();
      config.setMaxHandshakeTime(50);
      config.validate();
   }

   @Test
   public void emptyMonitoringValid() {
      HandshakeMonitorConfig config = new HandshakeMonitorConfig();
      config.setMonitoring(Collections.emptyList());
      config.validate();
   }

   @Test
   public void multipleMonitoringValid() {
      HandshakeMonitorConfig config = new HandshakeMonitorConfig();
      List<String> monitoring = new ArrayList<>();
      monitoring.add("a");
      monitoring.add("b");
      config.setMonitoring(monitoring);
      config.validate();
   }

   @Test
   public void copyTo() {
      HandshakeMonitorConfig config1 = new HandshakeMonitorConfig();
      config1.setMaxHandshakeTime(12);
      List<String> monitoring = new ArrayList<>();
      monitoring.add("a");
      monitoring.add("b");
      config1.setMonitoring(monitoring);
      HandshakeMonitorConfig config2 = new HandshakeMonitorConfig();
      config1.copyTo(config2);
      assertThat(config1.getMaxHandshakeTime(), is(config2.getMaxHandshakeTime()));
      assertThat(config1.getMonitoring(), is(config2.getMonitoring()));
   }
}