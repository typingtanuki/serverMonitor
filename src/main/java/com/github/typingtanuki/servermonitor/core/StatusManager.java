package com.github.typingtanuki.servermonitor.core;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.Status;
import com.github.typingtanuki.servermonitor.web.status.ClusterStatusResponse;
import com.github.typingtanuki.servermonitor.web.status.ShortStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.github.typingtanuki.servermonitor.utils.SimpleStack.simpleStack;

/**
 * Handles the status of this server
 */
public class StatusManager {
   private static final Logger logger = LoggerFactory.getLogger(StatusManager.class);

   private final MainConfig config;
   private Status status;

   public StatusManager(MainConfig config) {
      this.config = config;
   }

   public synchronized void updateStatus(List<MonitorReport> reports) {
      status = new Status(reports);
   }

   /**
    * @return The current status, in full
    */
   public synchronized Status getStatus() {
      if (status == null) {
         return new Status(Collections.emptyList());
      }
      return status;
   }

   /**
    * @return A short status of each node in the cluster
    */
   public ClusterStatusResponse getClusterStatus() {
      List<String> remotes = new ArrayList<>(config.getHandshake().getMonitoring());

      Map<String, Map<MonitorType, Boolean>> statusMap = new LinkedHashMap<>();
      Map<String, Map<String, Object>> advancedMap = new LinkedHashMap<>();
      Map<String, String> connections = new LinkedHashMap<>();
      addClusterStatus(statusMap,
                       new ShortStatusResponse(config.getIdentity(), getStatus()));
      for (String remote : remotes) {
         ShortStatusResponse remoteStatus = getRemoteStatus(remote);
         Map<String, Object> remoteAdvancedStatus = getAdvancedRemoteStatus(remote);
         connections.put(remoteStatus.getIdentity(), remote);
         advancedMap.put(remoteStatus.getIdentity(), remoteAdvancedStatus);
         addClusterStatus(statusMap, remoteStatus);
      }

      return new ClusterStatusResponse(
            config.getIdentity(),
            connections,
            statusMap,
            advancedMap);
   }

   private Map<String, Object> getAdvancedRemoteStatus(String remote) {
      RestCall<JsonStringObjectMap> call =
            new RestCall<>(remote, "/status", JsonStringObjectMap.class);
      try {
         return (Map<String, Object>) call.get().get("status");
      } catch (RestCallException e) {
         logger.debug("Could not get remote status: {}\r\n{}", remote, simpleStack(e));
         return new LinkedHashMap<>();
      }
   }

   private ShortStatusResponse getRemoteStatus(String remote) {
      RestCall<ShortStatusResponse> call =
            new RestCall<>(remote, "/status/short", ShortStatusResponse.class);
      try {
         return call.get();
      } catch (RestCallException e) {
         logger.debug("Could not get remote status: {}\r\n{}", remote, simpleStack(e));
         ShortStatusResponse fake = new ShortStatusResponse();
         fake.setIdentity(remote);
         fake.setStatus(Map.of(MonitorType.handshake, Boolean.FALSE));
         return fake;
      }
   }

   private void addClusterStatus(Map<String, Map<MonitorType, Boolean>> statuses,
                                 ShortStatusResponse toAdd) {
      statuses.put(toAdd.getIdentity(), toAdd.getStatus());
   }
}
