package com.github.typingtanuki.servermonitor;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.software.os.OSProcess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author clerc
 * @since 2020/01/24
 */
public class ServerMonitor {
    private final SystemInfo info;
    private final List<String> output = new LinkedList<>();
    private final MonitorConfig config;
    private boolean ng = false;

    public ServerMonitor() {
        super();
        info = new SystemInfo();
        config = new MonitorConfig();
    }

    public void startMonitoring() throws InterruptedException {
        while (true) {
            ng = false;
            output.clear();
            System.out.println("-----------------");
            monitorDisks();
            monitorCPU();
            monitorMemory();
            monitorProcesses();

            if (ng) {
                warnIssue();
            }
            Thread.sleep(config.monitorTime());
        }
    }

    private void monitorProcesses() {
        List<String> processes = config.processes();
        if (processes.isEmpty()) {
            return;
        }
        OSProcess[] current = info.getOperatingSystem().getProcesses();
        for (OSProcess c : current) {
            processes.removeIf(proc -> c.getName().contains(proc));
        }

        if (!processes.isEmpty()) {
            ng = true;
            String line = "Processes not running: " + String.join(", ", processes);
            output.add(line);
            System.out.println(line);
        } else {
            System.out.println("All processes are running");
        }
    }

    private void warnIssue() {
        System.out.println("Server is NG");
        System.out.println(info.getOperatingSystem());
        System.out.println(Arrays.toString(info.getHardware().getNetworkIFs()));
        for (String out : output) {
            System.out.println(out);
        }
    }

    private void monitorMemory() {
        GlobalMemory memory = info.getHardware().getMemory();

        long free = memory.getAvailable();
        long total = memory.getTotal();
        if (total > 0) {
            printUsage("Memory", usage(free, total));
        }
    }

    private void monitorCPU() {
        CentralProcessor processor = info.getHardware().getProcessor();
        double[] loads = processor.getSystemLoadAverage(1);
        printUsage("CPU", (long) loads[0]);
    }

    private void monitorDisks() {
        for (HWDiskStore store : info.getHardware().getDiskStores()) {
            for (HWPartition partition : store.getPartitions()) {
                String mount = partition.getMountPoint();
                Path disk = Paths.get(mount);
                long free = disk.toFile().getFreeSpace();
                long total = disk.toFile().getTotalSpace();
                if (total > 0) {
                    printUsage("Disk " + mount + " ", usage(free, total));
                }
            }
        }
    }

    private long usage(long free, long total) {
        return 100 - free * 100 / total;
    }


    private void printUsage(String msg, long usage) {
        MonitorStatus status = MonitorStatus.OK;
        if (usage > config.maxUsage()) {
            status = MonitorStatus.NG;
        }

        if (status == MonitorStatus.NG) {
            ng = true;
        }
        String line = msg + "\t" + usage + "%\t" + status;
        output.add(line);
        System.out.println(line);
    }

    public void loadConfig() throws IOException {
        Path configPath = Paths.get("./conf/monitor.conf");
        System.out.println(configPath.toFile().getAbsolutePath());
        if (!Files.exists(configPath)) {
            System.err.println("Missing config file");
            System.exit(1);
        }
        config.from(Files.readAllLines(configPath));
    }
}
