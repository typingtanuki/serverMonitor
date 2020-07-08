package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.monitors.Monitor;
import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public abstract class UpdateChecker implements Monitor {
   private static final Logger logger = LoggerFactory.getLogger(UpdateChecker.class);
   private static final Object currentCheckerLock = new Object[0];
   public static UpdateChecker currentChecker;
   private final MainConfig config;

   public UpdateChecker(MainConfig config) {
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
      List<UpdateChecker> possibleCheckers = new LinkedList<>();
      possibleCheckers.add(new YumChecker(config));
      // Prefer apt-get over apt as the API is more stable
      possibleCheckers.add(new AptGetChecker(config));
      possibleCheckers.add(new AptChecker(config));

      for (UpdateChecker updateChecker : possibleCheckers) {
         if (updateChecker.isAvailable()) {
            return updateChecker;
         }
      }

      logger.warn("Could not find suitable update checker, disabling");
      return new NoUpdateChecker();
   }

   public static String doUpdate() {
      UpdateChecker checker;
      synchronized (currentCheckerLock) {
         checker = currentChecker;
      }
      if (checker == null) {
         return "Not initialized";
      }

      return checker.runUpdate();
   }

   public abstract String runUpdate();

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
