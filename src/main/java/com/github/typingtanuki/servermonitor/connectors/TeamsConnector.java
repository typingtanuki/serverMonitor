package com.github.typingtanuki.servermonitor.connectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.report.MonitorReport;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import java.util.Date;
import java.util.List;

public class TeamsConnector implements Connector {
    private final MonitorConfig config;
    private final SystemInfo info;

    public TeamsConnector(MonitorConfig config, SystemInfo info) {
        super();

        this.config = config;
        this.info = info;
    }

    @Override
    public void reportFailure(List<MonitorReport> failedMonitorReports) {
        TeamsPayload payload = new TeamsPayload();
        payload.setSummary("Server is NG");
        TeamsSection osSection = new TeamsSection();
        osSection.setActivityTitle("Server " + config.identity() + " is NG");
        osSection.setActivitySubtitle("System details");
        osSection.addFact(new TeamsFact("identity", config.identity()));
        OperatingSystem os = info.getOperatingSystem();
        osSection.addFact(new TeamsFact("elevated", String.valueOf(os.isElevated())));
        osSection.addFact(new TeamsFact("family", String.valueOf(os.getFamily())));
        osSection.addFact(new TeamsFact("version", String.valueOf(os.getVersionInfo().toString())));
        payload.addSection(osSection);

        TeamsSection payloadSection = new TeamsSection();
        payloadSection.setActivityTitle("Server " + config.identity() + " is NG");
        payloadSection.setActivitySubtitle("Failed on " + new Date().toString());
        for (MonitorReport report : failedMonitorReports) {
            payloadSection.addFact(new TeamsFact(report.shortDescription(), "Failed"));
        }
        payload.addSection(payloadSection);
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writerFor(TeamsPayload.class).writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            System.out.println("Error preparing payload");
            e.printStackTrace();
        }
    }
}
