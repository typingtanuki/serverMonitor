package com.github.typingtanuki.servermonitor.connectors.teams;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TeamsFacts extends TeamsItem {
    private String type = "FactSet";
    private List<TeamsFact> facts = new LinkedList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TeamsFact> getFacts() {
        return facts;
    }

    public void setFacts(List<TeamsFact> facts) {
        this.facts = facts;
    }

    public void addFacts(TeamsFact... facts) {
        this.facts.addAll(Arrays.asList(facts));
    }

    public void addFact(TeamsFact fact) {
        this.facts.add(fact);
    }
}
