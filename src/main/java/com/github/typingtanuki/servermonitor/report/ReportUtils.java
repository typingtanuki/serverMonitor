package com.github.typingtanuki.servermonitor.report;

import java.text.DecimalFormat;

/**
 * @author clerc
 * @since 2020/01/29
 */
public final class ReportUtils {
    private ReportUtils() {
        super();
    }

    public static String bytesToHuman(long bytes) {
        if (bytes <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return new DecimalFormat("#,##0.#")
                .format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
