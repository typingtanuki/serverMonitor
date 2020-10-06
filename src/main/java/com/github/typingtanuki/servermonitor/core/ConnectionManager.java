package com.github.typingtanuki.servermonitor.core;

import java.util.HashMap;
import java.util.Map;

public final class ConnectionManager {
   private static final Object CONNECTION_LOCK = new Object[0];

   private static final Map<String, String> connections = new HashMap<>();

   private ConnectionManager() {
      super();
   }

   public static void addConnection(String name, String host) {
      synchronized (CONNECTION_LOCK) {
         connections.put(name, host);
      }
   }

   public static void addUnknownConnection(String name) {
      if (connections.containsKey(name)) {
         return;
      }

      synchronized (CONNECTION_LOCK) {
         connections.put(name, name);
      }
   }

   public static Map<String, String> getConnections() {
      synchronized (CONNECTION_LOCK) {
         return new HashMap<>(connections);
      }
   }
}
