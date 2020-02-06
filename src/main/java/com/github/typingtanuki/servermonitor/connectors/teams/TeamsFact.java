package com.github.typingtanuki.servermonitor.connectors.teams;

/**
 * A fact is a line in the section
 *
 * It is key/value based
 */
@SuppressWarnings("unused")
public class TeamsFact {
    private String name;
    private String value;

    public TeamsFact() {
        super();
    }

    public TeamsFact(String name, String value) {
        super();

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
