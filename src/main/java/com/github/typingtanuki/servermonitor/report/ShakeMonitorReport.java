package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.timestampToHuman;

public class ShakeMonitorReport extends AbstractBoolMonitorReport {
    private String reason;
    private long request;
    private long response;
    private String cause;
    private Long maxDelta;

    public ShakeMonitorReport(String target) {
        super(target);
    }

    public void noConnect(String cause) {
        ng();
        reason = "Could not connect";
        this.cause = cause;
    }

    public void pingTooLong(long request, long response, long maxDelta) {
        this.request = request;
        this.response = response;
        this.maxDelta = maxDelta;
        ng();
        reason = "Ping took too long";
    }

    public void pingBackInTime(long request, long response, long maxDelta) {
        this.request = request;
        this.response = response;
        this.maxDelta = maxDelta;
        ng();
        reason = "Ping went back in time";
    }

    public void ok(long request, long response, long maxDelta) {
        this.request = request;
        this.response = response;
        this.maxDelta = maxDelta;
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
        out.put("Server", monitored);
        if (reason != null) {
            out.put("reason", reason);
        }
        if (request > 0 && response > 0) {
            out.put("Request time", timestampToHuman(request));
            out.put("Response time", timestampToHuman(response));
            out.put("Delta", response - request);
            out.put("Max delta", maxDelta);
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
