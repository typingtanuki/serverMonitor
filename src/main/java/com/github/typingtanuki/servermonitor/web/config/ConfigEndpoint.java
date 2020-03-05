package com.github.typingtanuki.servermonitor.web.config;

import com.github.typingtanuki.servermonitor.MonitorMain;
import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.core.ServerMonitor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * @author clerc
 * @since 2020/02/07
 */
@Path("/config")
public class ConfigEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MainConfig fetchSettings() {
        ServerMonitor monitor = MonitorMain.monitor;
        return monitor.currentConfig();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MainConfig updateSettings(@QueryParam("persist") boolean persist,
                                     MainConfig newConfig) throws IOException {
        ServerMonitor monitor = MonitorMain.monitor;
        return monitor.updateConfig(newConfig, persist);
    }

    @POST
    @Path("/doUpdate")
    @Produces(MediaType.TEXT_PLAIN)
    public String doUpdate() {
        return MonitorMain.monitor.doUpdate();
    }
}
