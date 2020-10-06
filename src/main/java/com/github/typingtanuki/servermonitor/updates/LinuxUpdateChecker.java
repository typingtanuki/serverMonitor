package com.github.typingtanuki.servermonitor.updates;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public abstract class LinuxUpdateChecker extends UpdateChecker {
   protected static final Logger logger =
         LoggerFactory.getLogger(LinuxUpdateChecker.class);

   protected LinuxUpdateChecker(MainConfig config) {
      super(config);
   }

   protected boolean isGoodOs() {
      return System.getProperty("os.name")
                   .toLowerCase(Locale.ENGLISH)
                   .startsWith("linux");
   }

   @Override
   protected String binaryCheckCommand() {
      return "which";
   }

   @Override
   protected boolean isAvailable() {
      if (!isGoodOs()) {
         logger.info("Not good os for {}", binaryName());
         return false;
      }
      if (!hasBinary()) {
         logger.info("Binary {} not installed", binaryName());
         return false;
      }
      logger.info("Checking updates for {}", binaryName());
      return true;
   }
}
