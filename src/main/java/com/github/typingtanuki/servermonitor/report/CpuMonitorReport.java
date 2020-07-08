package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.core.History;
import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

public class CpuMonitorReport extends AbstractPercentMonitorReport {
   private final History history;
   private final Map<String, String> topProcesses;

   public CpuMonitorReport(long load,
                           History history,
                           int maxUsage,
                           Map<String, String> topProcesses) {
      super(load, maxUsage);
      this.history = history;
      this.topProcesses = new LinkedHashMap<>(topProcesses);
   }

   @Override
   public String getTitle() {
      return "CPU usage";
   }

   @Override
   public String getDescription() {
      return "CPU usage " + usage + "% (Maximum allowed " + maxUsage + "%)";
   }

   @Override
   public Map<DetailKey, Object> getDetails() {
      Map<DetailKey, Object> out = new LinkedHashMap<>();
      out.put(DetailKey.USAGE_CURRENT, usage + "%");
      out.put(DetailKey.USAGE_MAX, maxUsage + "%");
      out.put(DetailKey.HISTORY, history);
      out.put(DetailKey.TOP, topProcesses);
      return out;
   }

   @Override
   public MonitorType getType() {
      return MonitorType.cpu;
   }

   @Override
   public MonitorCategory getCategory() {
      return MonitorCategory.system;
   }
}
