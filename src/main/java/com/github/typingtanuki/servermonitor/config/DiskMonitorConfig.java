package com.github.typingtanuki.servermonitor.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Settings for the disk monitors
 */
public class DiskMonitorConfig extends MonitorConfig {
   /** The usage (in percent) over which we are triggering a warning */
   private int maxUsage = 80;
   /**
    * The list of extra mount points we are to monitor
    * <p>
    * In most cases the mounts will be auto-detected and the list can be kept empty.
    * Mounts will be de-duplicated.
    */
   private List<String> mounts = new ArrayList<>();

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
         this.mounts = new ArrayList<>();
         return;
      }
      this.mounts = mounts;
   }
}
