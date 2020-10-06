package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.core.ConnectionManager;
import com.github.typingtanuki.servermonitor.core.RestCall;
import com.github.typingtanuki.servermonitor.core.RestCallException;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.ShakeMonitorReport;
import com.github.typingtanuki.servermonitor.web.handshake.HandshakeResponse;
import oshi.SystemInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.now;

/**
 * Sends a handshake to another monitor and waits for response
 * <p>
 * What is considered a failure:
 * <ul>
 *     <li>Could not connect to remote monitor</li>
 *     <li>Response timestamp is ahead in time from request response</li>
 *     <li>Remote monitor took too long to respond</li>
 * </ul>
 */
public class HandshakeMonitor implements Monitor {
   private final MainConfig config;
   private final Map<String, String> lastSeen = new LinkedHashMap<>();

   public HandshakeMonitor(MainConfig config) {
      super();
      this.config = config;
   }

   @Override
   public List<MonitorReport> monitor(SystemInfo systemInfo) {
      List<String> targets = config.getHandshake().getMonitoring();
      int maxHandshakeTime = config.getHandshake().getMaxHandshakeTime();

      List<MonitorReport> out = new ArrayList<>(targets.size());
      for (String target : targets) {
         ShakeMonitorReport monitor = new ShakeMonitorReport(target);
         shakeHand(target, monitor, maxHandshakeTime);
         out.add(monitor);
      }

      List<String> removed = new ArrayList<>();
      for (String key : lastSeen.keySet()) {
         if (!targets.contains(key)) {
            removed.add(key);
         }
      }
      for (String r : removed) {
         lastSeen.remove(r);
      }

      return out;
   }

   @Override
   public boolean isEnabled() {
      if (!config.getHandshake().isEnabled()) {
         return false;
      }
      List<String> monitoring = config.getHandshake().getMonitoring();
      return monitoring != null && !monitoring.isEmpty();
   }

   @Override
   public MonitorType getType() {
      return MonitorType.handshake;
   }

   @Override
   public MonitorCategory getCategory() {
      return MonitorCategory.remote;
   }

   private void shakeHand(String target,
                          ShakeMonitorReport monitor,
                          int maxHandshakeTime) {
      ConnectionManager.addUnknownConnection(target);
      RestCall<HandshakeResponse> call = new RestCall<>(target,
                                                        "/handshake?request=" +
                                                        System.currentTimeMillis(),
                                                        HandshakeResponse.class);
      HandshakeResponse handshake;
      try {
         handshake = call.get();
      } catch (RestCallException e) {
         monitor.noConnect(
               e.shortMessage(),
               lastSeen.get(target));
         return;
      }


      //Validate the response content
      if (handshake.getRequestTime() - handshake.getResponseTime() > maxHandshakeTime) {
         monitor.pingBackInTime(
               handshake.getRequestTime(),
               handshake.getResponseTime(),
               maxHandshakeTime,
               lastSeen.get(target));
         return;
      }

      if (handshake.getResponseTime() - handshake.getRequestTime() > maxHandshakeTime) {
         monitor.pingTooLong(
               handshake.getRequestTime(),
               handshake.getResponseTime(),
               maxHandshakeTime,
               lastSeen.get(target));
         return;
      }
      monitor.ok(handshake.getRequestTime(),
                 handshake.getResponseTime(),
                 maxHandshakeTime);
      lastSeen.put(target, now());
   }
}
