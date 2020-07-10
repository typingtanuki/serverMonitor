package com.github.typingtanuki.servermonitor.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Small wrapper for doing a REST call and de-serializing the response
 *
 * @param <T> The type of the expected response
 */
public class RestCall<T> {
   private final ObjectReader reader;
   private final String url;

   /**
    * Create a new REST "client"
    *
    * @param baseUrl    the hostname, ip, ... with port and without http prefix
    * @param path       the URL path, starting with a /
    * @param resultType the class to deserialize the response to
    */
   public RestCall(String baseUrl, String path, Class<T> resultType) {
      this.url = "http://" + baseUrl + path;
      this.reader = new ObjectMapper().readerFor(resultType);
   }

   /**
    * Do an HTTP GET no the endpoint
    *
    * @return The de-serialized response
    * @throws RestCallException If we could not connect to the server or could not read
    *                           the response
    */
   public T get() throws RestCallException {
      // Sends the handshake request and wait for response
      Response response;
      ClientBuilder builder = ResteasyClientBuilder.newBuilder();
      builder.connectTimeout(10, TimeUnit.SECONDS);

      Client client = builder.build();
      try {
         WebTarget resource = client.target(url);
         Invocation.Builder request = resource.request();
         request.accept("application/json");
         response = request.buildGet().invoke();
      } catch (RuntimeException e) {
         client.close();
         throw new RestCallException("Could not connect", e);
      }
      if (response.getStatus() != 200) {
         client.close();
         throw new RestCallException("Status " + response.getStatus() + " not 200");
      }

      // Read the response
      try {
         String output = response.readEntity(String.class);
         return reader.readValue(output);
      } catch (IOException e) {
         throw new RestCallException("Could not de-serialize response", e);
      } finally {
         client.close();
      }
   }
}
