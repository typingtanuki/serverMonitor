package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.core.History;
import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.bytesToHuman;

public class MemoryMonitorReport extends AbstractPercentMonitorReport {
   private final History history;
   private final Map<String, String> topProcesses;

   public MemoryMonitorReport(long free,
                              long total,
                              History history,
                              int maxUsage,
                              Map<String, String> topProcesses) {
      super(free, total, maxUsage);
      this.history = history;
      this.topProcesses = topProcesses;
   }

   @Override
   public String getTitle() {
      return "Memory usage";
   }

   @Override
   public String getDescription() {
      return "Memory usage " + usage + "% (" +
             "Maximum allowed " + maxUsage + "%) " +
             "Free: " + bytesToHuman(free) + " " +
             "Total: " + bytesToHuman(total);
   }

   @Override
   public Map<DetailKey, Object> getDetails() {
      Map<DetailKey, Object> out = new LinkedHashMap<>();
      out.put(DetailKey.USAGE_CURRENT, usage + "%");
      out.put(DetailKey.USAGE_MAX, maxUsage + "%");
      out.put(DetailKey.MEMORY_FREE, bytesToHuman(free));
      out.put(DetailKey.MEMORY_TOTAL, bytesToHuman(total));
      out.put(DetailKey.HISTORY, history);
      out.put(DetailKey.TOP, topProcesses);
      return out;
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
