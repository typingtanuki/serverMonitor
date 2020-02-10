package com.github.typingtanuki.servermonitor.monitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import com.github.typingtanuki.servermonitor.report.ShakeMonitorReport;
import com.github.typingtanuki.servermonitor.web.handshake.HandshakeResponse;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import oshi.SystemInfo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
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
    private static final ObjectReader reader = new ObjectMapper().readerFor(HandshakeResponse.class);
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

    private void shakeHand(String target,
                           ShakeMonitorReport monitor,
                           int maxHandshakeTime) {
        // Sends the handshake request and wait for response
        Response response;
        try {
            ResteasyClientBuilder builder = new ResteasyClientBuilder();
            Client client = builder.build();
            WebTarget resource = client.target("http://" + target + "/handshake?request=" + System.currentTimeMillis());
            Invocation.Builder request = resource.request();
            request.accept("application/json");
            response = request.buildGet().invoke();
        } catch (RuntimeException e) {
            e.printStackTrace();
            monitor.noConnect(e.getMessage());
            return;
        }
        if (response.getStatus() != 200) {
            monitor.noConnect("Status " + response.getStatus() + " not 200");
            return;
        }

        // Read the response
        HandshakeResponse handshake;
        try {
            String output = response.readEntity(String.class);
            handshake = reader.readValue(output);
        } catch (IOException e) {
            e.printStackTrace();
            monitor.noConnect(e.getMessage());
            return;
        }

        //Validate the response content
        if (handshake.getResponseTime() < handshake.getRequestTime()) {
            monitor.pingBackInTime(handshake.getRequestTime(), handshake.getResponseTime());
            return;
        }

        if (handshake.getResponseTime() - handshake.getRequestTime() > maxHandshakeTime) {
            monitor.pingTooLong(handshake.getRequestTime(), handshake.getResponseTime());
            return;
        }
        monitor.ok(handshake.getRequestTime(), handshake.getResponseTime());
    }
}
