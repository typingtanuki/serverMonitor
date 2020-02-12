package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.DiskMonitorReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Monitors disk (partition) usage against a max allowed percentage
 */
public class DiskMonitor implements Monitor {
    private MainConfig config;

    public DiskMonitor(MainConfig config) {
        super();

        this.config = config;
    }


    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<MonitorReport> out = new LinkedList<>();
        int maxDiskUsage = config.getDisk().getMaxUsage();

        for (HWDiskStore store : systemInfo.getHardware().getDiskStores()) {
            for (HWPartition partition : store.getPartitions()) {
                String mount = partition.getMountPoint();
                Path disk = Paths.get(mount);
                long free = disk.toFile().getFreeSpace();
                long total = disk.toFile().getTotalSpace();
                if (total > 0) {
                    out.add(new DiskMonitorReport(mount, free, total, maxDiskUsage));
                }
            }
        }

        return out;
    }

    @Override
    public boolean isEnabled() {
        return config.getDisk().isEnabled();
    }

    @Override
    public MonitorType getType() {
        return MonitorType.disk;
    }
}
