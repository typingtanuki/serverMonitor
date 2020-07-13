package com.github.typingtanuki.servermonitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UpdateSigner {
   public static void main(String... args) {
      if (args.length != 2) {
         System.err.println("Need 2 arguments:" +
                            "\r\n - The path to the file to sign" +
                            "\r\n - The path to the certificate to generate");
         System.exit(1);
      }

      Path toSign = Paths.get(args[0]);
      Path certificate = Paths.get(args[1]);

      if (!Files.exists(toSign)) {
         System.err.println("File to sign does not exist");
         System.exit(1);
      }

      try {
         ZipSigner.sign(toSign, certificate);
      } catch (IOException e) {
         System.err.println("Could not generate certificate");
         e.printStackTrace();
         System.exit(1);
      }
   }
}
