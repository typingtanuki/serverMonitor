package com.github.typingtanuki.servermonitor.connectors.teams;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class TeamsPayload {
    @JsonProperty("@type")
    private final String type = "MessageCard";
    @JsonProperty("@context")
    private final String context = "http://schema.org/extensions";
    private String summary;
    private List<TeamsSection> sections = new LinkedList<>();

    public String getType() {
        return type;
    }

    public String getContext() {
        return context;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<TeamsSection> getSections() {
        return sections;
    }

    public void setSections(List<TeamsSection> sections) {
        this.sections = sections;
    }

    public void addSection(TeamsSection section) {
        sections.add(section);
    }
}
