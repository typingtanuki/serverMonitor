package com.github.typingtanuki.servermonitor.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;
import com.github.typingtanuki.servermonitor.updates.UpdateReport;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NetworkMonitorReport.class, name = "network"),
        @JsonSubTypes.Type(value = UpdateReport.class, name = "update"),
        @JsonSubTypes.Type(value = ProcessMonitorReport.class, name = "process"),
        @JsonSubTypes.Type(value = ShakeMonitorReport.class, name = "handshake"),
        @JsonSubTypes.Type(value = PingMonitorReport.class, name = "ping"),
        @JsonSubTypes.Type(value = MemoryMonitorReport.class, name = "memory"),
        @JsonSubTypes.Type(value = DiskMonitorReport.class, name = "disk"),
        @JsonSubTypes.Type(value = CpuMonitorReport.class, name = "cpu")
})
public interface MonitorReport {
    boolean isOK();

    String getTitle();

    String getDescription();

    Map<DetailKey, Object> getDetails();

    MonitorType getType();

    MonitorCategory getCategory();

    String monitorKey();
}
