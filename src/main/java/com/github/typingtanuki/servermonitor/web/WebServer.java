package com.github.typingtanuki.servermonitor.web;

import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.web.config.ConfigEndpoint;
import com.github.typingtanuki.servermonitor.web.handshake.HandshakeEndpoint;
import com.github.typingtanuki.servermonitor.web.site.SiteEndpoint;
import com.github.typingtanuki.servermonitor.web.status.StatusEndpoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jetty.JettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class WebServer extends Thread {
   private final int port;
   private Server jettyServer;

   public WebServer(MainConfig mainConfig) {
      super();

      this.port = mainConfig.getPort();
   }

   public void startServer() throws IOException {
      ResourceConfig config = new ResourceConfig();
      config.register(new CORSFilter());
      config.register(JacksonFeature.class);
      config.register(JettyHttpContainerProvider.class);
      config.property(ServerProperties.BV_FEATURE_DISABLE, Boolean.TRUE);
      config.property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, Boolean.TRUE);
      config.register(JacksonJaxbJsonProvider.class);

      //Jersey servlet <> Jetty binding
      ServletHolder holder = new ServletHolder(new ServletContainer(config));

      //Tell jetty jersey path starts from / and is responsible for /*
      ServletContextHandler servlet =
            new ServletContextHandler(ServletContextHandler.SESSIONS);
      servlet.addServlet(holder, "/*");
      servlet.setSessionHandler(new SessionHandler());

      jettyServer = new Server(port);
      jettyServer.setHandler(servlet);

      Set<Class<?>> endpoints = new LinkedHashSet<>();
      endpoints.add(HandshakeEndpoint.class);
      endpoints.add(StatusEndpoint.class);
      endpoints.add(ConfigEndpoint.class);
      endpoints.add(SiteEndpoint.class);
      config.registerClasses(endpoints);

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
