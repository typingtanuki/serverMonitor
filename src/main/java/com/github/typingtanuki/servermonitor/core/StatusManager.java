package com.github.typingtanuki.servermonitor.core;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.Status;
import com.github.typingtanuki.servermonitor.web.status.ClusterStatusResponse;
import com.github.typingtanuki.servermonitor.web.status.SimpleStatus;

import java.util.*;

/**
 * Handles the status of this server
 */
public class StatusManager {
   private static final Object CLUSTER_STATUS_LOCK = new Object[0];

   private final MainConfig config;
   private Status status;

   private Map<String, SimpleStatus> areaStatus = Collections.emptyMap();
   private List<MonitorReport> fastReports = Collections.emptyList();
   private List<MonitorReport> slowReports = Collections.emptyList();
   private Map<String, Map<MonitorType, Boolean>> areaShortStatus =
         Collections.emptyMap();

   public StatusManager(MainConfig config) {
      this.config = config;
   }

   public synchronized void updateStatus(List<MonitorReport> newFastReports,
                                         List<MonitorReport> newSlowReports) {
      List<MonitorReport> all = new ArrayList<>();
      if (newFastReports != null) {
         fastReports = newFastReports;
      }
      all.addAll(fastReports);

      if (newSlowReports != null) {
         slowReports = newSlowReports;
      }
      all.addAll(slowReports);
      status = new Status(all);
   }

   public synchronized void updateAreaStatus(Map<String, SimpleStatus> areaStatus) {
      synchronized (CLUSTER_STATUS_LOCK) {
         this.areaStatus = new HashMap<>(areaStatus);
      }
   }

   public synchronized void updateAreaShortStatus(
         Map<String, Map<MonitorType, Boolean>> areaShortStatus) {
      synchronized (CLUSTER_STATUS_LOCK) {
         this.areaShortStatus = new HashMap<>(areaShortStatus);
      }
   }

   /**
    * Status machine
    *
    * @return The current status, in full
    */
   public synchronized Status getStatus() {
      if (status == null) {
         return new Status(Collections.emptyList());
      }
      return status;
   }

   /**
    * Status of the full cluster
    *
    * @return A short status of each node in the cluster
    */
   public ClusterStatusResponse getClusterStatus() {
      synchronized (CLUSTER_STATUS_LOCK) {
         Map<String, String> connections = new LinkedHashMap<>();

         return new ClusterStatusResponse(
               config.getIdentity(),
               connections,
               areaShortStatus,
               areaStatus);
      }
   }
}
