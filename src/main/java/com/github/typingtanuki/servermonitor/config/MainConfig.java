package com.github.typingtanuki.servermonitor.config;

public class MainConfig {
    private String identity = null;
    private long monitorTime = 30_000;
    private long debounceTime = 86_400_000;
    private int port = 9191;

    private String teamsHook = null;

    private CpuMonitorConfig cpu = new CpuMonitorConfig();
    private DiskMonitorConfig disk = new DiskMonitorConfig();
    private MemoryMonitorConfig memory = new MemoryMonitorConfig();
    private ProcessMonitorConfig process = new ProcessMonitorConfig();
    private PingMonitorConfig ping = new PingMonitorConfig();
    private HandshakeMonitorConfig handshake = new HandshakeMonitorConfig();
    private UpdateMonitorConfig updates = new UpdateMonitorConfig();
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
            throw new IllegalStateException("Monitor time should be between monitorTime and 1 day");
        }

        if (port < 1 || port > 65535) {
            throw new IllegalStateException("REST port should be between 1 and 65535");
        }

        cpu.validate();
        disk.validate();
        memory.validate();
        process.validate();
        ping.validate();
        handshake.validate();
        updates.validate();
        network.validate();
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
