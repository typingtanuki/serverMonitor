package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShakeMonitorReport extends AbstractBoolMonitorReport {
    private String reason;
    private long request;
    private long response;
    private String cause;

    public ShakeMonitorReport(String target) {
        super(target);
    }

    public void noConnect(String cause) {
        ng();
        reason = "Could not connect";
        this.cause = cause;
    }

    public void pingTooLong(long request, long response) {
        this.request = request;
        this.response = response;
        ng();
        reason = "Ping took too long";
    }

    public void pingBackInTime(long request, long response) {
        this.request = request;
        this.response = response;
        ng();
        reason = "Ping went back in time";
    }

    public void ok(long request, long response) {
        this.request = request;
        this.response = response;
        ok();
    }

    @Override
    public String getTitle() {
        return "Handshake test";
    }

    @Override
    public String getDescription() {
        return "Handshake to " + monitored;
    }

    @Override
    public Map<String, Object> getDetails() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("server", monitored);
        out.put("reason", reason);
        if (request > 0 && response > 0) {
            out.put("Request time", request);
            out.put("Response time", response);
        }
        if (cause != null) {
            out.put("Cause", cause);
        }
        return out;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.handshake;
    }
}
