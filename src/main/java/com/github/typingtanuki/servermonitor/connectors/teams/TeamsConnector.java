package com.github.typingtanuki.servermonitor.connectors.teams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.connectors.Connector;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Connector for Microsoft Teams
 */
public class TeamsConnector implements Connector {
    private static Logger logger = LoggerFactory.getLogger(TeamsConnector.class);

    private final MonitorConfig config;
    private final SystemInfo info;

    public TeamsConnector(MonitorConfig config, SystemInfo info) {
        super();

        this.config = config;
        this.info = info;
    }

    @Override
    public void reportFailure(MonitorReport failedMonitorReport) {
        TeamsPayload payload = new TeamsPayload();
        payload.setSummary("Server is NG");

        // Section with the OS details
        TeamsSection osSection = new TeamsSection();
        osSection.setActivityTitle("Server " + config.identity() + " is NG");
        osSection.setActivitySubtitle("System details");
        osSection.addFact(new TeamsFact("identity", config.identity()));
        OperatingSystem os = info.getOperatingSystem();
        osSection.addFact(new TeamsFact("elevated", String.valueOf(os.isElevated())));
        osSection.addFact(new TeamsFact("family", String.valueOf(os.getFamily())));
        osSection.addFact(new TeamsFact("version", String.valueOf(os.getVersionInfo().toString())));
        payload.addSection(osSection);

        // Section with the failure information
        TeamsSection payloadSection = new TeamsSection();
        payloadSection.setActivityTitle(failedMonitorReport.title());
        payloadSection.setActivitySubtitle(failedMonitorReport.shortDescription());

        for (Map.Entry<String, Object> details : failedMonitorReport.details().entrySet()) {
            payloadSection.addFact(new TeamsFact(details.getKey(), String.valueOf(details.getValue())));
        }
        payload.addSection(payloadSection);

        sendPayload(payload);
    }

    /**
     * Convert the payload to JSON and send it to the hook
     *
     * @param payload The payload to send
     */
    private void sendPayload(TeamsPayload payload) {
        ObjectMapper mapper = new ObjectMapper();
        String payloadJson;

        try {
            payloadJson = mapper.writerFor(TeamsPayload.class).writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            logger.warn("Failure while preparing payload", e);
            return;
        }
        sendPayload(payloadJson);
    }

    /**
     * Send the string (json) payload to the hook
     *
     * @param payloadJson The json-ified payload to send
     */
    private void sendPayload(String payloadJson) {
        ResteasyClientBuilder builder = new ResteasyClientBuilder();
        Client client = builder.build();
        WebTarget resource = client.target(config.teamsHook());
        Invocation.Builder request = resource.request();
        request.accept("application/json");
        Response response = request.buildPost(Entity.json(payloadJson)).invoke();
        if (response.getStatus() != 200) {
            logger.warn("API responded with wrong error code {}", response.getStatus());
        }
    }
}
