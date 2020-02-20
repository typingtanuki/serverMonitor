package com.github.typingtanuki.servermonitor.report;

import com.github.typingtanuki.servermonitor.monitors.MonitorCategory;
import com.github.typingtanuki.servermonitor.monitors.MonitorType;

import java.util.LinkedHashMap;
import java.util.Map;

public class PingMonitorReport extends AbstractBoolMonitorReport {
    private String method;
    private String cause;

    public PingMonitorReport(String monitored) {
        super(monitored);
    }

    @Override
    public String getTitle() {
        return "Ping test";
    }

    @Override
    public String getDescription() {
        return "Ping to " + monitored;
    }

    @Override
    public Map<String, Object> getDetails() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("Server", monitored);
        if (cause != null) {
            out.put("Cause", cause);
        } else {
            out.put("Method", method);
        }
        return out;
    }

    @Override
    public MonitorType getType() {
        return MonitorType.ping;
    }

    @Override
    public MonitorCategory getCategory() {
        return MonitorCategory.remote;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void ok(String method) {
        this.method = method;
        this.cause = null;
        super.ok();
    }

    public void ng(Exception cause) {
        this.cause = cause.getMessage();
        super.ng();
    }

}
