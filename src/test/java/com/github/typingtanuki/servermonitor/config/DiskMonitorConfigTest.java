package com.github.typingtanuki.servermonitor.config;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class DiskMonitorConfigTest {
   @Test
   public void defaultValid() {
      new DiskMonitorConfig().validate();
   }

   @Test
   public void invalidUsageUnder() {
      DiskMonitorConfig config = new DiskMonitorConfig();
      config.setMaxUsage(0);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidUsageOver() {
      DiskMonitorConfig config = new DiskMonitorConfig();
      config.setMaxUsage(100);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void validUsage() {
      DiskMonitorConfig config = new DiskMonitorConfig();
      config.setMaxUsage(50);
      config.validate();
   }

   @Test
   public void emptyMountsValid() {
      DiskMonitorConfig config = new DiskMonitorConfig();
      config.setMounts(Collections.emptyList());
      config.validate();
   }

   @Test
   public void multipleMountsValid() {
      DiskMonitorConfig config = new DiskMonitorConfig();
      List<String> mounts = new ArrayList<>();
      mounts.add("a");
      mounts.add("b");
      config.setMounts(mounts);
      config.validate();
   }

   @Test
   public void copyTo() {
      DiskMonitorConfig config1 = new DiskMonitorConfig();
      config1.setMaxUsage(12);
      List<String> mounts = new ArrayList<>();
      mounts.add("a");
      mounts.add("b");
      config1.setMounts(mounts);
      DiskMonitorConfig config2 = new DiskMonitorConfig();
      config1.copyTo(config2);
      assertThat(config1.getMaxUsage(), is(config2.getMaxUsage()));
      assertThat(config1.getMounts(), is(config2.getMounts()));
   }
}