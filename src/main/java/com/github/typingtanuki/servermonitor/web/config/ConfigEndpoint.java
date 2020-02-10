package com.github.typingtanuki.servermonitor.web.config;

import com.github.typingtanuki.servermonitor.MonitorMain;
import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.monitors.ServerMonitor;

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
    public MonitorConfig fetchSettings() {
        ServerMonitor monitor = MonitorMain.monitor;
        return monitor.fetchSettings();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MonitorConfig updateSettings(@QueryParam("persist") boolean persist,
                                        MonitorConfig newConfig) throws IOException {
        ServerMonitor monitor = MonitorMain.monitor;
        return monitor.updateSettings(newConfig, persist);
    }
}
