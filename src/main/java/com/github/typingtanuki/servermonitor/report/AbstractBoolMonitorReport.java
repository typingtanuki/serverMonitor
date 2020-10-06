package com.github.typingtanuki.servermonitor.report;

public abstract class AbstractBoolMonitorReport implements MonitorReport {
   protected String monitored;
   private boolean ng = true;

   @Deprecated
   protected AbstractBoolMonitorReport() {
      super();
   }

   protected AbstractBoolMonitorReport(String monitored) {
      super();

      this.monitored = monitored;
   }

   public void ok() {
      ng = false;
   }

   public void ng() {
      ng = true;
   }

   @Override
   public boolean isOK() {
      return !ng;
   }

   @Override
   public String monitorKey() {
      return getType() + monitored;
   }
}
