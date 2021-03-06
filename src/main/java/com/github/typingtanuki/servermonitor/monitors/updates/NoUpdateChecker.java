package com.github.typingtanuki.servermonitor.monitors.updates;

import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;

import java.util.Collections;
import java.util.List;

public class NoUpdateChecker extends UpdateChecker {
   public NoUpdateChecker() {
      super(null);
   }

   @Override
   protected String binaryName() {
      return null;
   }

   @Override
   protected String binaryCheckCommand() {
      return null;
   }

   @Override
   public boolean isEnabled() {
      return false;
   }

   @Override
   protected boolean isAvailable() {
      return false;
   }

   @Override
   public List<MonitorReport> monitor(SystemInfo systemInfo) {
      return Collections.emptyList();
   }
}
