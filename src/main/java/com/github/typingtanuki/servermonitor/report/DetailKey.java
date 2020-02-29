package com.github.typingtanuki.servermonitor.report;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DetailKey {
    SERVER("Server"),
    CAUSE("Cause"),
    REQUEST_TIME("Request time"),
    RESPONSE_TIME("Response time"),
    DELTA("Delta"),
    MAX_DELTA("Max delta"),
    REASON("Reason"),
    LAST_SEEN("Last Seen"),
    METHOD("Method"),
    HISTORY("History"),
    HISTORY_DATES("History Dates"),
    MEMORY_TOTAL("Total memory"),
    MEMORY_FREE("Free memory"),
    USAGE_MAX("Maximum Usage"),
    USAGE_CURRENT("Current Usage"),
    PROCESS("Process"),
    PID("PID"),
    UPTIME("Uptime"),
    NAME("Name"),
    COMMAND_LINE("Command Line"),
    DISK("Disk"),
    SPACE_FREE("Free space"),
    SPACE_TOTAL("Total space"),
    UPDATES("Updates");

    private final String value;

    DetailKey(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
