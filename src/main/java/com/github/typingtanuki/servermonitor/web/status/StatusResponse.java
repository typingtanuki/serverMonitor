package com.github.typingtanuki.servermonitor.web.status;

import com.github.typingtanuki.servermonitor.report.Status;

/**
 * @author clerc
 * @since 2020/02/07
 */
public class StatusResponse {
    private final String identity;
    private final Status status;

    public StatusResponse(String identity, Status status) {
        super();
        this.identity = identity;

        this.status = status;
    }

    public String getIdentity() {
        return identity;
    }

    public Status getStatus() {
        return status;
    }
}
