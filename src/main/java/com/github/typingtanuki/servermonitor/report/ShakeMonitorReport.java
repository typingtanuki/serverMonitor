package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.millisToHuman;
import static com.github.typingtanuki.servermonitor.report.ReportUtils.timestampToHuman;

public class ShakeMonitorReport extends AbstractBoolMonitorReport {
    private String reason;
    private long request;
    private long response;
    private String cause;
    private Long maxDelta;
    private String lastSeen;

    public ShakeMonitorReport(String target) {
        super(target);
    }

    public void noConnect(String cause, String lastSeen) {
        ng();
        reason = "Could not connect";
        this.cause = cause;
        this.lastSeen = lastSeen;
    }

    public void pingTooLong(long request, long response, long maxDelta, String lastSeen) {
        this.request = request;
        this.response = response;
        this.maxDelta = maxDelta;
        this.lastSeen = lastSeen;
        ng();
        reason = "Ping took too long";
    }

    public void pingBackInTime(long request, long response, long maxDelta, String lastSeen) {
        this.request = request;
        this.response = response;
        this.maxDelta = maxDelta;
        this.lastSeen = lastSeen;
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
    public Map<DetailKey, Object> getDetails() {
        Map<DetailKey, Object> out = new LinkedHashMap<>();
        out.put(DetailKey.SERVER, monitored);
        if (reason != null) {
            out.put(DetailKey.REASON, reason);
        }
        if (request > 0 && response > 0) {
            out.put(DetailKey.REQUEST_TIME, timestampToHuman(request));
            out.put(DetailKey.RESPONSE_TIME, timestampToHuman(response));
            out.put(DetailKey.DELTA, millisToHuman(response - request));
            out.put(DetailKey.MAX_DELTA, millisToHuman(maxDelta));
        }
        if (cause != null) {
            out.put(DetailKey.CAUSE, cause);
        }
        if (lastSeen != null) {
            out.put(DetailKey.LAST_SEEN, lastSeen);
        }
        return out;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.handshake;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.remote;
    }
}
