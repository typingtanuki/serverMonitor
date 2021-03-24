package com.github.typingtanuki.servermonitor.web.site;

import com.github.typingtanuki.servermonitor.MonitorMain;
import com.github.typingtanuki.servermonitor.core.ServerMonitor;
import jakarta.servlet.http.HttpServletResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/")
public class SiteEndpoint {
    @GET
    public HttpServletResponse redirect(@Context final HttpServletResponse response)
            throws IOException {
        response.sendRedirect("/www/index.html");
        return response;
    }

    @GET
    @Path("/www")
    public HttpServletResponse getRoot(@Context final HttpServletResponse response)
            throws IOException {
        response.sendRedirect("/www/index.html");
        return response;
    }

    @GET
    @Path("/www/{path:.*}")
    public Response getSubPage(@PathParam("path") String path) throws IOException {
        ServerMonitor monitor = MonitorMain.monitor;
        return monitor.wwwServer().serve(path);
    }
}
