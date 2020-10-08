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
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Jetty web server
 */
public class WebServer extends Thread {
   private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

   /** Port on which we want to listen for REST or HTML requests */
   private final int port;
   /** The Jetty server instance */
   private Server jettyServer;

   public WebServer(MainConfig mainConfig) {
      super();

      this.port = mainConfig.getPort();
   }

   /** Start Jetty web server */
   public void startServer() throws IOException {
      ResourceConfig config = createConfig();

      //Jersey servlet <> Jetty binding
      ServletHolder holder = new ServletHolder(new ServletContainer(config));

      //Tell jetty jersey path starts from / and is responsible for /*
      ServletContextHandler servlet =
            new ServletContextHandler(ServletContextHandler.SESSIONS);
      servlet.addServlet(holder, "/*");
      servlet.setSessionHandler(new SessionHandler());

      jettyServer = new Server(port);
      jettyServer.setHandler(servlet);

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

   /** Creates the Jetty configuration */
   private static ResourceConfig createConfig() {
      ResourceConfig config = new ResourceConfig();
      config.register(new CORSFilter());
      config.register(JacksonFeature.class);
      config.register(MultiPartFeature.class);
      config.register(JettyHttpContainerProvider.class);
      config.property(ServerProperties.BV_FEATURE_DISABLE, Boolean.TRUE);
      config.property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, Boolean.TRUE);
      config.register(JacksonJaxbJsonProvider.class);

      config.registerClasses(endpoints());

      return config;
   }

   /** List of endpoints exposed through the REST API */
   private static Set<Class<?>> endpoints() {
      Set<Class<?>> endpoints = new LinkedHashSet<>();
      // REST endpoints
      endpoints.add(HandshakeEndpoint.class);
      endpoints.add(StatusEndpoint.class);
      endpoints.add(ConfigEndpoint.class);
      // HTTP endpoint
      endpoints.add(SiteEndpoint.class);
      return endpoints;
   }

   @Override
   public void run() {
      try {
         jettyServer.join();
      } catch (Exception e) {
         logger.error("Failed starting webserver", e);
         e.printStackTrace();
      } finally {
         jettyServer.destroy();
      }
   }
}
