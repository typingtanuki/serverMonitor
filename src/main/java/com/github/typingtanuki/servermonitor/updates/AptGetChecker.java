package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Uses apt-get cached data to check for update.
 * <p>
 * Updating the cache itself requires admin priviledges
 */
public class AptGetChecker extends LinuxUpdateChecker {
   private static final Pattern APT_GET_LINE =
         Pattern.compile("[^\\s]+\\s+([^\\s]+)\\s+\\[([^]]+)\\].*");

   public AptGetChecker(MainConfig config) {
      super(config);
   }

   @Override
   public String runUpdate() {
      List<String> out = new LinkedList<>();
      out.addAll(runAndReadOutput("sudo", "apt-get", "update"));
      out.addAll(runAndReadOutput("sudo", "apt-get", "upgrade", "-y"));
      out.addAll(runAndReadOutput("sudo", "apt-get", "dist-upgrade", "-y"));
      out.addAll(runAndReadOutput("sudo", "apt-get", "autoremove", "-y"));
      return String.join("\r\n", out);
   }

   @Override
   protected String binaryName() {
      return "apt-get";
   }


   @Override
   public List<MonitorReport> monitor(SystemInfo systemInfo) {
      List<String> out = runAndReadOutput("apt-get", "--just-print", "upgrade");

      if (out == null) {
         return Collections.singletonList(new InvalidReport(getType(), getCategory()));
      }

      Map<String, String> updates = new LinkedHashMap<>();
      for (String line : out) {
         Matcher matcher = APT_GET_LINE.matcher(line);
         if (matcher.matches()) {
            updates.put(matcher.group(1), matcher.group(2));
         }
      }
      return Collections.singletonList(new UpdateReport(updates));
   }
}
