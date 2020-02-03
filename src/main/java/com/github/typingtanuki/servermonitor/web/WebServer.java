package com.github.typingtanuki.servermonitor.web;

import com.github.typingtanuki.servermonitor.config.MonitorConfig;
import com.github.typingtanuki.servermonitor.web.handshake.HandshakeEndpoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;

public class WebServer extends Thread {
    private final int port;
    private Server jettyServer;

    public WebServer(MonitorConfig monitorConfig) {
        super();

        this.port = monitorConfig.handshakePort();
    }

    public void startServer() throws IOException {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        jettyServer = new Server(port);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                HandshakeEndpoint.class.getCanonicalName());

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
