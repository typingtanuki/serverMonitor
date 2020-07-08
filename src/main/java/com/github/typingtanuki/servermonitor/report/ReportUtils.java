package com.github.typingtanuki.servermonitor.report;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author clerc
 * @since 2020/01/29
 */
public final class ReportUtils {
   private static final DateFormat FORMATTER =
         new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

   private ReportUtils() {
      super();
   }

   public static String now() {
      return FORMATTER.format(new Date());
   }

   public static String bytesToHuman(long bytes) {
      if (bytes <= 0) {
         return "0";
      }
      final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
      int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
      return new DecimalFormat("#,##0.#")
                   .format(bytes / Math.pow(1024, digitGroups)) +
             " " +
             units[digitGroups];
   }

   public static String timestampToHuman(long ts) {
      Date date = new Date(ts);
      DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      return formatter.format(date);
   }


   public static String millisToHuman(long duration) {
      if (duration < 0) {
         return "-" + millisToHumanAbs(-duration);
      }
      return millisToHumanAbs(duration);
   }

   private static String millisToHumanAbs(long duration) {
      if (duration <= 1000) {
         return duration + "ms";
      }
      if (duration <= 60_000) {
         return (duration / 1000) + "s";
      }
      if (duration <= 3_600_000) {
         long inMin = duration / 60_000;
         return inMin + "min " + millisToHuman(duration - inMin * 60_000);
      }
      long inHour = duration / 3_600_000;
      return inHour + "hour " + millisToHuman(duration - inHour * 3_600_000);
   }
}
