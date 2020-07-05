package com.github.typingtanuki.servermonitor.web.status;

import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.Map;

public class ClusterStatusResponse {
    private final String identity;
    private final Map<String, Map<MonitorType, Boolean>> clusterStatus;
    private final Map<String, Map<String, Object>> advanced;
    private final Map<String, String> connections;

    public ClusterStatusResponse(String identity,
                                 Map<String, String> connections,
                                 Map<String, Map<MonitorType, Boolean>> clusterStatus,
                                 Map<String, Map<String, Object>> advanced) {
        this.identity = identity;
        this.connections = connections;
        this.clusterStatus = clusterStatus;
        this.advanced = advanced;
    }

    public String getIdentity() {
        return identity;
    }

    public Map<String, Map<MonitorType, Boolean>> getClusterStatus() {
        return clusterStatus;
    }

    public Map<String, String> getConnections() {
        return connections;
    }

    public Map<String, Map<String, Object>> getAdvanced() {
        return advanced;
    }
}
