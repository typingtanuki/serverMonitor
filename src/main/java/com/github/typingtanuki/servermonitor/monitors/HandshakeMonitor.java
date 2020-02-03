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

public class HandshakeMonitor implements Monitor {
    private static final ObjectReader reader = new ObjectMapper().readerFor(HandshakeResponse.class);
    private final List<String> targets;
    private final int maxHandshakeTime;

    public HandshakeMonitor(MonitorConfig config) {
        super();

        targets = config.handshake();
        maxHandshakeTime = config.maxHandshakeTime();
    }

    @Override
    public List<MonitorReport> monitor(SystemInfo systemInfo) {
        List<MonitorReport> out = new ArrayList<>(targets.size());
        for (String target : targets) {
            ShakeMonitorReport monitor = new ShakeMonitorReport(target);
            shakeHand(target, monitor);
            out.add(monitor);
        }
        return out;
    }

    private void shakeHand(String target,
                           ShakeMonitorReport monitor) {
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
            System.out.println("Status " + response.getStatus());
            monitor.noConnect("Status " + response.getStatus() + " not 200");
            return;
        }

        HandshakeResponse handshake;
        try {
            String output = response.readEntity(String.class);
            handshake = reader.readValue(output);
        } catch (IOException e) {
            e.printStackTrace();
            monitor.noConnect(e.getMessage());
            return;
        }

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
