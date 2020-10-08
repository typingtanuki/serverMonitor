package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.monitors.Monitor;
import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.github.typingtanuki.servermonitor.utils.SimpleStack.simpleStack;

public abstract class UpdateChecker implements Monitor {
   private static final Logger logger = LoggerFactory.getLogger(UpdateChecker.class);
   private static final Object currentCheckerLock = new Object[0];
   public static UpdateChecker currentChecker;
   private final MainConfig config;

   protected UpdateChecker(MainConfig config) {
      super();

      this.config = config;
   }

   public static UpdateChecker bestChecker(MainConfig config) {
      if (currentChecker != null) {
         return currentChecker;
      }
      synchronized (currentCheckerLock) {
         if (currentChecker != null) {
            return currentChecker;
         }
         currentChecker = findChecker(config);
      }
      return currentChecker;
   }

   private static UpdateChecker findChecker(MainConfig config) {
      logger.info("Looking for best update checker");
      // Ordered list of checkers
      List<UpdateChecker> possibleCheckers = new ArrayList<>();
      possibleCheckers.add(new YumChecker(config));
      // Prefer apt-get over apt as the API is more stable
      possibleCheckers.add(new AptGetChecker(config));
      possibleCheckers.add(new AptChecker(config));
      // Powershell checker
      possibleCheckers.add(new PowershellUpdateChecker(config));

      for (UpdateChecker updateChecker : possibleCheckers) {
         if (updateChecker.isAvailable()) {
            return updateChecker;
         }
      }

      logger.warn("Could not find suitable update checker, disabling");
      return new NoUpdateChecker();
   }

   protected List<String> runAndReadOutput(String... commands) {
      ProcessBuilder builder = new ProcessBuilder(commands);
      List<String> out;
      try {
         out = runAndReadOutput(builder);
      } catch (IOException e) {
         logger.warn("Failed to get upgrade list\r\n{}", simpleStack(e));
         return null;
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         logger.warn("Interrupted while getting upgrade list\r\n{}", simpleStack(e));
         return null;
      }
      return out;
   }

   @SuppressWarnings("DefaultCharset" /* Use default charset to match command line */)
   protected List<String> runAndReadOutput(ProcessBuilder builder)
         throws IOException, InterruptedException {
      builder.redirectErrorStream(true);

      Process process = builder.start();
      InputStreamReader isReader = new InputStreamReader(process.getInputStream());
      BufferedReader reader = new BufferedReader(isReader);
      List<String> output = new ArrayList<>();
      while (process.isAlive()) {
         Thread.sleep(100);
         swallowOutput(reader, output);
      }
      Thread.sleep(100);
      swallowOutput(reader, output);
      return output;
   }

   private void swallowOutput(BufferedReader reader, List<String> output)
         throws IOException {
      String str;
      while ((str = reader.readLine()) != null) {
         if (!str.isBlank()) {
            output.add(str.strip());
         }
      }
   }

   protected abstract String binaryName();

   protected abstract String binaryCheckCommand();

   protected boolean hasBinary() {
      String binary = binaryName();

      ProcessBuilder builder = new ProcessBuilder(binaryCheckCommand(), binary);
      List<String> out;
      try {
         out = runAndReadOutput(builder);
      } catch (IOException e) {
         logger.warn("Could not check for command {}\r\n{}",
                     binary,
                     simpleStack(e));
         return false;
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         logger.warn("Interrupted while checking for command {}\r\n{}",
                     binary,
                     simpleStack(e));
         return false;
      }
      return !out.isEmpty();
   }

   @Override
   public final MonitorType getType() {
      return MonitorType.update;
   }

   @Override
   public final MonitorCategory getCategory() {
      return MonitorCategory.system;
   }

   @Override
   public boolean isEnabled() {
      return config.getUpdates().isEnabled();
   }

   protected MainConfig getConfig() {
      return config;
   }

   protected abstract boolean isAvailable();
}
