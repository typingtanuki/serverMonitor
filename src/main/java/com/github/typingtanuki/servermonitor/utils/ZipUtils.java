package com.github.typingtanuki.servermonitor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipUtils {
   private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

   private ZipUtils() {
      super();
   }

   public static void unzip(Path uploadedZip, Path updateFolder) throws IOException {
      Files.createDirectories(updateFolder);

      try (FileInputStream fis = new FileInputStream(uploadedZip.toFile())) {
         try (ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
               if (!entry.isDirectory()) {
                  String fileName = entry.getName();

                  Path target = updateFolder.resolve(fileName);
                  if (!Files.exists(target.getParent())) {
                     Files.createDirectories(target.getParent());
                     logger.info("Creating  {}", target.getParent().toAbsolutePath());
                  }
                  logger.info("Unzipping {} to {}", fileName, target.toAbsolutePath());

                  Files.copy(zis, target, StandardCopyOption.REPLACE_EXISTING);
               }
               //close this ZipEntry
               zis.closeEntry();
               entry = zis.getNextEntry();
            }
         }
      }
   }
}
