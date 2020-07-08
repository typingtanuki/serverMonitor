package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.report.InvalidReport;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Experimental, APT CLI is still in beta
 */
public class AptChecker extends LinuxUpdateChecker {
   private static final Pattern APT_LINE =
         Pattern.compile("([^/]+)/[^\\s]+\\s+([^\\s]+)\\s+.*");

   public AptChecker(MainConfig config) {
      super(config);
   }

   @Override
   public String runUpdate() {
      List<String> out = new LinkedList<>();
      out.addAll(runAndReadOutput("sudo", "apt", "update"));
      out.addAll(runAndReadOutput("sudo", "apt", "upgrade", "-y"));
      out.addAll(runAndReadOutput("sudo", "apt", "dist-upgrade", "-y"));
      out.addAll(runAndReadOutput("sudo", "apt", "autoremove", "-y"));
      return String.join("\r\n", out);
   }

   @Override
   protected String binaryName() {
      return "apt";
   }

   @Override
   public List<MonitorReport> monitor(SystemInfo systemInfo) {
      List<String> out = runAndReadOutput("apt", "list", "--upgradable");

      if (out == null) {
         return Collections.singletonList(new InvalidReport(getType(), getCategory()));
      }

      Map<String, String> updates = new LinkedHashMap<>();
      for (String line : out) {
         Matcher matcher = APT_LINE.matcher(line);
         if (matcher.matches()) {
            updates.put(matcher.group(1), matcher.group(2));
         }
      }

      return Collections.singletonList(new UpdateReport(updates));
   }
}
