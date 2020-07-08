package com.github.typingtanuki.servermonitor.report;

public abstract class AbstractBoolMonitorReport implements MonitorReport {
   protected final String monitored;
   private boolean ng = true;

   public AbstractBoolMonitorReport(String monitored) {
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
