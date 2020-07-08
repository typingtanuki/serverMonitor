package com.github.typingtanuki.servermonitor.config;

import java.util.LinkedList;
import java.util.List;

public class DiskMonitorConfig extends MonitorConfig {
   private int maxUsage = 80;
   private List<String> mounts = new LinkedList<>();

   public DiskMonitorConfig() {
      super();
   }

   @Override
   public void validate() {
      if (maxUsage < 1 || maxUsage > 99) {
         throw new IllegalStateException("Disk usage should be between 1 and 99%");
      }
   }

   public void copyTo(DiskMonitorConfig targetConfig) {
      innerCopyTo(targetConfig);
      targetConfig.maxUsage = maxUsage;
      targetConfig.mounts = mounts;
   }

   public int getMaxUsage() {
      return maxUsage;
   }

   public void setMaxUsage(int maxUsage) {
      this.maxUsage = maxUsage;
   }

   public List<String> getMounts() {
      return mounts;
   }

   public void setMounts(List<String> mounts) {
      if (mounts == null) {
         this.mounts = new LinkedList<>();
         return;
      }
      this.mounts = mounts;
   }
}
