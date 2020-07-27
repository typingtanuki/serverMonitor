package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.DiskMonitorReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Monitors disk (partition) usage against a max allowed percentage
 */
public class DiskMonitor implements Monitor {
   private static final Logger logger = LoggerFactory.getLogger(DiskMonitor.class);

   private MainConfig config;

   public DiskMonitor(MainConfig config) {
      super();

      this.config = config;
   }


   @Override
   public List<MonitorReport> monitor(SystemInfo systemInfo) {
      List<MonitorReport> out = new LinkedList<>();
      int maxDiskUsage = config.getDisk().getMaxUsage();

      Set<String> extraPath = new LinkedHashSet<>();
      Set<String> skip = new LinkedHashSet<>();
      if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("linux")) {
         extraPath.add("/"); // Common root
         extraPath.add("/home"); // Often, home is a different disk
         skip.add("/boot/efi"); // Fixed partition used for boot
      } else {
         extraPath.add("c:\\");
      }
      // User defined paths
      extraPath.addAll(config.getDisk().getMounts());

      for (HWDiskStore store : systemInfo.getHardware().getDiskStores()) {
         for (HWPartition partition : store.getPartitions()) {
            String mount = partition.getMountPoint();
            extraPath.remove(mount.toLowerCase(Locale.ENGLISH));
            if (!skip.contains(mount.toLowerCase(Locale.ENGLISH))) {
               out.addAll(monitorPartition(mount, maxDiskUsage));
            }
         }
      }

      for (String extra : extraPath) {
         out.addAll(monitorPartition(extra, maxDiskUsage));
      }
      return out;
   }

   private List<MonitorReport> monitorPartition(String mount, int maxDiskUsage) {
      if (mount.isBlank()) {
         return Collections.emptyList();
      }
      Path disk = Paths.get(mount);
      long free = disk.toFile().getFreeSpace();
      long total = disk.toFile().getTotalSpace();
      if (total > 0) {
         return Collections.singletonList(new DiskMonitorReport(mount,
                                                                free,
                                                                total,
                                                                maxDiskUsage));
      }
      logger.warn("Could not monitor {}, total is 0", mount);
      return Collections.emptyList();
   }

   @Override
   public boolean isEnabled() {
      return config.getDisk().isEnabled();
   }

   @Override
   public MonitorType getType() {
      return MonitorType.disk;
   }

   @Override
   public MonitorCategory getCategory() {
      return MonitorCategory.system;
   }
}
