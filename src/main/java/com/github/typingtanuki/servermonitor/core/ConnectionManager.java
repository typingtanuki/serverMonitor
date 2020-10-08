package com.github.typingtanuki.servermonitor.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Keep a mapping between server aliases and IP/hostname
 */
public final class ConnectionManager {
   private static final Object CONNECTION_LOCK = new Object[0];

   private static final Map<String, String> connections = new HashMap<>();

   private ConnectionManager() {
      super();
   }

   /**
    * Register a new alias
    *
    * @param name The name of the server
    * @param host The IP/host for the server
    */
   public static void addConnection(String name, String host) {
      synchronized (CONNECTION_LOCK) {
         connections.put(name, host);
      }
   }

   /**
    * Register a default alias (host→host)
    * <p>
    * If there is already a registered alias, it will be skipped
    *
    * @param host The host of the server
    */
   public static void addUnknownConnection(String host) {
      if (connections.containsKey(host)) {
         return;
      }

      synchronized (CONNECTION_LOCK) {
         connections.put(host, host);
      }
   }

   /** Get a copy of the current connections */
   public static Map<String, String> getConnections() {
      synchronized (CONNECTION_LOCK) {
         return new HashMap<>(connections);
      }
   }
}
