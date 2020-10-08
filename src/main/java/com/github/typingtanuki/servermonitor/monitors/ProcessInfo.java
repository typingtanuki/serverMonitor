package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.core.History;
import oshi.software.os.OSProcess;

/**
 * Details on a running process
 *
 * @author clerc
 * @since 2020/07/03
 */
public class ProcessInfo {
   private final History cpuHistory = new History();
   private final History memoryHistory = new History();

   private String name;
   private boolean running = false;
   private int pid = -1;
   private long uptime = -1;
   private String commandLine = null;
   private OSProcess lastInstance;
   private long currentCpu;
   private long currentMemory;
   private long memoryPercent;

   public ProcessInfo(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public boolean isRunning() {
      return running;
   }

   public void setRunning(boolean running) {
      this.running = running;
   }

   public int getPid() {
      return pid;
   }

   public long getUptime() {
      return uptime;
   }

   public String getCommandLine() {
      return commandLine;
   }

   public History getCpuHistory() {
      return cpuHistory;
   }

   public History getMemoryHistory() {
      return memoryHistory;
   }

   public long getCurrentCpu() {
      return currentCpu;
   }

   public long getCurrentMemory() {
      return currentMemory;
   }

   public long getMemoryPercent() {
      return memoryPercent;
   }

   public void fromProcess(OSProcess c, long totalMemory, int historySize) {
      running = true;
      pid = c.getProcessID();
      uptime = c.getUpTime();
      name = c.getName();
      commandLine = c.getCommandLine();

      currentCpu = (long) (100L * c.getProcessCpuLoadBetweenTicks(lastInstance));
      currentMemory = c.getVirtualSize();
      memoryPercent = (long) ((double) (100L * currentMemory) / totalMemory);

      if (lastInstance != null) {
         cpuHistory.touch(currentCpu, historySize, 80L);
         memoryHistory.touch(memoryPercent, historySize, 80L);
      }
      lastInstance = c;
   }

   public void missing(int historySize) {
      running = false;
      cpuHistory.touch(0L, historySize, 80L);
      memoryHistory.touch(0L, historySize, 80L);
      lastInstance = null;
   }
}
