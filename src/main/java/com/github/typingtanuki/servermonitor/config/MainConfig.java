package com.github.typingtanuki.servermonitor.config;

/**
 * Configuration for the entire server monitor
 */
public class MainConfig {
   /** The identity of this server (hostname, ip, nickname, ...) */
   private String identity = null;
   /** The interval at which the different monitors will be checked (in ms) */
   private long monitorTime = 30_000;
   /** The cool-down period during which we do not re-trigger a notification (in ms) */
   private long debounceTime = 86_400_000;
   /** The port on which to listen for connection (web and REST) */
   private int port = 9191;

   /** Hook for the teams hook (or any office365 hook) */
   private String teamsHook = null;

   /** Settings for CPU monitoring */
   private CpuMonitorConfig cpu = new CpuMonitorConfig();
   /** Settings for disk monitoring */
   private DiskMonitorConfig disk = new DiskMonitorConfig();
   /** Settings for memory monitoring */
   private MemoryMonitorConfig memory = new MemoryMonitorConfig();
   /** Settings for process monitoring */
   private ProcessMonitorConfig process = new ProcessMonitorConfig();
   /** Settings for ping-based server monitoring */
   private PingMonitorConfig ping = new PingMonitorConfig();
   /** Settings for handshake-based server monitoring */
   private HandshakeMonitorConfig handshake = new HandshakeMonitorConfig();
   /** Settings for the OS update monitoring */
   private UpdateMonitorConfig updates = new UpdateMonitorConfig();
   /** Settings for the network load monitoring */
   private NetworkMonitorConfig network = new NetworkMonitorConfig();

   public void copyTo(MainConfig config) {
      config.identity = identity;
      config.monitorTime = monitorTime;
      config.debounceTime = debounceTime;
      config.port = port;
      config.teamsHook = teamsHook;

      cpu.copyTo(config.cpu);
      disk.copyTo(config.disk);
      memory.copyTo(config.memory);
      process.copyTo(config.process);
      ping.copyTo(config.ping);
      handshake.copyTo(config.handshake);
      updates.copyTo(config.updates);
      network.copyTo(config.network);
   }

   public void validate() {
      if (identity == null || identity.isBlank()) {
         throw new IllegalStateException("Missing identity in settings");
      }
      if (monitorTime < 1000 || monitorTime > 3_600_000) {
         throw new IllegalStateException("Monitor time should be between 1s and 1 hour");
      }

      if (debounceTime < monitorTime || debounceTime > 3_600_000 * 24) {
         throw new IllegalStateException(
               "Monitor time should be between monitorTime and 1 day");
      }

      if (port < 1 || port > 65535) {
         throw new IllegalStateException("REST port should be between 1 and 65535");
      }

      validateSub("cpu", cpu);
      validateSub("disk", disk);
      validateSub("memory", memory);
      validateSub("process", process);
      validateSub("ping", ping);
      validateSub("handshake", handshake);
      validateSub("updates", updates);
      validateSub("network", network);
   }

   private static void validateSub(String type, MonitorConfig monitorConfig) {
      if (monitorConfig == null) {
         throw new IllegalStateException("Settings " + type + " are null.");
      }
      try {
         monitorConfig.validate();
      } catch (IllegalStateException e) {
         throw new IllegalStateException("Failed to validate settings for " + type, e);
      }
   }

   public String getIdentity() {
      return identity;
   }

   public void setIdentity(String identity) {
      this.identity = identity;
   }

   public long getMonitorTime() {
      return monitorTime;
   }

   public void setMonitorTime(long monitorTime) {
      this.monitorTime = monitorTime;
   }

   public long getDebounceTime() {
      return debounceTime;
   }

   public void setDebounceTime(long debounceTime) {
      this.debounceTime = debounceTime;
   }

   public int getPort() {
      return port;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public String getTeamsHook() {
      return teamsHook;
   }

   public void setTeamsHook(String teamsHook) {
      this.teamsHook = teamsHook;
   }

   public CpuMonitorConfig getCpu() {
      return cpu;
   }

   public void setCpu(CpuMonitorConfig cpu) {
      this.cpu = cpu;
   }

   public DiskMonitorConfig getDisk() {
      return disk;
   }

   public void setDisk(DiskMonitorConfig disk) {
      this.disk = disk;
   }

   public MemoryMonitorConfig getMemory() {
      return memory;
   }

   public void setMemory(MemoryMonitorConfig memory) {
      this.memory = memory;
   }

   public ProcessMonitorConfig getProcess() {
      return process;
   }

   public void setProcess(ProcessMonitorConfig process) {
      this.process = process;
   }

   public PingMonitorConfig getPing() {
      return ping;
   }

   public void setPing(PingMonitorConfig ping) {
      this.ping = ping;
   }

   public HandshakeMonitorConfig getHandshake() {
      return handshake;
   }

   public void setHandshake(HandshakeMonitorConfig handshake) {
      this.handshake = handshake;
   }

   public UpdateMonitorConfig getUpdates() {
      return updates;
   }

   public void setUpdates(UpdateMonitorConfig updates) {
      this.updates = updates;
   }

   public NetworkMonitorConfig getNetwork() {
      return network;
   }

   public void setNetwork(NetworkMonitorConfig network) {
      this.network = network;
   }
}
