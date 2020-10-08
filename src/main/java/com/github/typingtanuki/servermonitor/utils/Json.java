package com.github.typingtanuki.servermonitor.utils;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class Json {
   private static final ObjectWriter PRETTY_PRINTER =
         new ObjectMapper().writerWithDefaultPrettyPrinter();

   public static String pretty(Object instance) throws IOException {
      try {
         return PRETTY_PRINTER.writeValueAsString(instance);
      } catch (JsonMappingException e) {
         throw new IOException("Could not convert config object to JSON", e);
      }
   }
}
