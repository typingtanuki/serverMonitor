package com.github.typingtanuki.servermonitor.monitors;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.core.RestCall;
import com.github.typingtanuki.servermonitor.core.RestCallException;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.ShakeMonitorReport;
import com.github.typingtanuki.servermonitor.web.handshake.HandshakeResponse;
import oshi.SystemInfo;

import java.util.ArrayList;
import java.util.List;

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
    private final MonitorConfig config;

    public HandshakeMonitor(MonitorConfig config) {
        super();
        this.config = config;
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<String> targets = config.getHandshake();
        int maxHandshakeTime = config.getMaxHandshakeTime();

        List<MonitorReport> out = new ArrayList<>(targets.size());
        for (String target : targets) {
            ShakeMonitorReport monitor = new ShakeMonitorReport(target);
            shakeHand(target, monitor, maxHandshakeTime);
            out.add(monitor);
        }
        return out;
    }

    @Override
    public boolean isEnabled() {
        return config.getHandshake() != null && !config.getHandshake().isEmpty();
    }

    @Override
    public MonitorType getType() {
        return MonitorType.handshake;
    }

    private void shakeHand(String target,
                           ShakeMonitorReport monitor,
                           int maxHandshakeTime) {
        RestCall<HandshakeResponse> call = new RestCall<>(target, "/handshake?request=" + System.currentTimeMillis(), HandshakeResponse.class);
        HandshakeResponse handshake;
        try {
            handshake = call.get();
        } catch (RestCallException e) {
            monitor.noConnect(e.shortMessage());
            return;
        }

        //Validate the response content
        if (handshake.getResponseTime() < handshake.getRequestTime()) {
            monitor.pingBackInTime(handshake.getRequestTime(), handshake.getResponseTime());
            return;
        }

        if (handshake.getResponseTime() - handshake.getRequestTime() > maxHandshakeTime) {
            monitor.pingTooLong(handshake.getRequestTime(), handshake.getResponseTime(), maxHandshakeTime);
            return;
        }
        monitor.ok(handshake.getRequestTime(), handshake.getResponseTime(), maxHandshakeTime);
    }
}
