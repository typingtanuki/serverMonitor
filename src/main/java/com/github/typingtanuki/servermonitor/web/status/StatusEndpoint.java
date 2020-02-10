package com.github.typingtanuki.servermonitor.web.status;

import com.github.typingtanuki.servermonitor.MonitorMain;
import com.github.typingtanuki.servermonitor.monitors.ServerMonitor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author clerc
 * @since 2020/02/07
 */
@Path("/status")
public class StatusEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public StatusResponse status() {
        ServerMonitor monitor = MonitorMain.monitor;
        return new StatusResponse(monitor.fetchSettings().getIdentity(), monitor.getStatus());
    }
}
