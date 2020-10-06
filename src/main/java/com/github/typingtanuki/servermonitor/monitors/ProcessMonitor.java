package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.ProcessMonitorReport;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Monitors running processes on the server.
 * <p>
 * If an expected process is not running, a failure will be raised
 */
public class ProcessMonitor implements Monitor {
   private final MainConfig config;
   private final Map<String, ProcessInfo> infos = new HashMap<>();

   public ProcessMonitor(MainConfig config) {
      super();

      this.config = config;
   }

   @Override
   public List<MonitorReport> monitor(SystemInfo systemInfo) {
      GlobalMemory memory = systemInfo.getHardware().getMemory();

      List<String> processes = new ArrayList<>(config.getProcess().getMonitoring());

      List<OSProcess> current = systemInfo.getOperatingSystem()
                                          .getProcesses(0,
                                                        OperatingSystem.ProcessSort.PID);
      List<MonitorReport> out = new ArrayList<>();

      List<String> keys = new ArrayList<>(infos.keySet());
      for (String key : keys) {
         if (!processes.contains(key)) {
            infos.remove(key);
         }
      }

      for (String proc : processes) {
         ProcessMonitorReport report = new ProcessMonitorReport(proc);

         ProcessInfo info = getOrInit(proc);
         info.setRunning(false);

         for (OSProcess c : current) {
            if (c.getName().contains(proc) || c.getCommandLine().contains(proc)) {
               info.fromProcess(
                     c,
                     memory.getTotal(),
                     config.getProcess().getHistorySize());
               break;
            }
         }
         if (info.isRunning()) {
            report.ok(info);
         } else {
            info.missing(config.getProcess().getHistorySize());
            report.ng(info);
         }
         out.add(report);
      }

      return out;
   }

   private ProcessInfo getOrInit(String proc) {
      ProcessInfo out = infos.get(proc);
      if (out != null) {
         return out;
      }
      out = new ProcessInfo(proc);
      infos.put(proc, out);
      return out;
   }

   @Override
   public boolean isEnabled() {
      if (!config.getProcess().isEnabled()) {
         return false;
      }
      List<String> monitoring = config.getProcess().getMonitoring();
      return monitoring != null && !monitoring.isEmpty();
   }

   @Override
   public MonitorType getType() {
      return MonitorType.process;
   }

   @Override
   public MonitorCategory getCategory() {
      return MonitorCategory.system;
   }
}
