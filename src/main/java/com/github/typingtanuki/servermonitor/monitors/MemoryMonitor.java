package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.core.History;
import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MemoryMonitorReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.AbstractPercentMonitorReport.usage;
import static com.github.typingtanuki.servermonitor.report.ReportUtils.bytesToHuman;

/**
 * Monitors Memory usage against a max allowed percentage
 */
public class MemoryMonitor implements Monitor {
   private final MainConfig config;
   private final History history = new History(100);
   private Map<Integer, Long> prevTop = new LinkedHashMap<>();

   public MemoryMonitor(MainConfig config) {
      super();

      this.config = config;
   }

   @Override
   public List<MonitorReport> monitor(SystemInfo systemInfo) {
      GlobalMemory memory = systemInfo.getHardware().getMemory();

      long free = memory.getAvailable();
      long total = memory.getTotal();
      history.touch(usage(free, total),
                    config.getMemory().getHistorySize(),
                    (long) config.getMemory().getMaxUsage());

      if (total <= 0) {
         return Collections.singletonList(new InvalidReport(getType(), getCategory()));
      }

      return Collections.singletonList(new MemoryMonitorReport(
            free,
            total,
            history,
            config.getMemory().getMaxUsage(),
            getTopProcesses(systemInfo.getOperatingSystem())));
   }

   private Map<String, String> getTopProcesses(OperatingSystem operatingSystem) {
      Map<String, String> top = new LinkedHashMap<>();
      List<OSProcess> topMemory =
            operatingSystem.getProcesses(10, OperatingSystem.ProcessSort.MEMORY);
      Map<Integer, Long> newTop = new LinkedHashMap<>();
      for (OSProcess process : topMemory) {
         int pid = process.getProcessID();
         long memory = process.getVirtualSize();
         newTop.put(pid, memory);
         top.put(pid + " " + process.getName(),
                 bytesToHuman(memory) + " " + compare(prevTop.get(pid), memory));
      }
      prevTop = newTop;
      return top;
   }


   private String compare(Long prev, long cpu) {
      if (prev == null) {
         return "new";
      }
      if (prev - 1024 < cpu && cpu < prev + 1024) {
         return "-";
      }
      if (prev > cpu) {
         return "↓" + bytesToHuman(prev - cpu);
      }
      return "↑" + bytesToHuman(cpu - prev);
   }

   @Override
   public boolean isEnabled() {
      return config.getMemory().isEnabled();
   }

   @Override
   public MonitorType getType() {
      return MonitorType.memory;
   }

   @Override
   public MonitorCategory getCategory() {
      return MonitorCategory.system;
   }
}
