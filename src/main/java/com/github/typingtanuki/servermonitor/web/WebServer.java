package com.github.typingtanuki.servermonitor.web;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.web.handshake.HandshakeEndpoint;
import com.github.typingtanuki.servermonitor.web.config.ConfigEndpoint;
import com.github.typingtanuki.servermonitor.web.site.SiteEndpoint;
import com.github.typingtanuki.servermonitor.web.status.StatusEndpoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WebServer extends Thread {
    private final int port;
    private Server jettyServer;

    public WebServer(MainConfig mainConfig) {
        super();

        this.port = mainConfig.getPort();
    }

    public void startServer() throws IOException {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        jettyServer = new Server(port);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        List<Class<?>> endpoints = new LinkedList<>();
        endpoints.add(HandshakeEndpoint.class);
        endpoints.add(StatusEndpoint.class);
        endpoints.add(ConfigEndpoint.class);
        endpoints.add(SiteEndpoint.class);

        StringBuilder classnames = new StringBuilder();
        for (Class<?> endpoint : endpoints) {
            if (classnames.length() > 0) {
                classnames.append(";");
            }
            classnames.append(endpoint.getCanonicalName());
        }

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                classnames.toString());

        try {
            jettyServer.start();
        } catch (Exception e) {
            try {
                jettyServer.destroy();
            } catch (IllegalStateException drop) {
                // Drop exception
            }
            throw new IOException("Error starting server", e);
        }
    }

    @Override
    public void run() {
        try {
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
        }
    }
}
