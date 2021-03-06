package com.github.typingtanuki.servermonitor.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * Small wrapper for doing a REST call and de-serializing the response
 *
 * @param <T> The type of the expected response
 */
public class RestCall<T> {
   private static final Logger logger = LoggerFactory.getLogger(RestCall.class);

   /** Default timeout for the remote server to respond */
   private static final long DEFAULT_GET_TIMEOUT = 10_000L;

   /** JSON reader for the given class */
   private final ObjectReader reader;
   /** The URL being connected to */
   private final String url;
   /** The class of the response */
   private final Class<T> resultType;

   /**
    * Create a new REST "client"
    *
    * @param baseUrl    the hostname, ip, ... with port and without http prefix
    * @param path       the URL path, starting with a /
    * @param resultType the class to deserialize the response to
    */
   public RestCall(String baseUrl, String path, Class<T> resultType) {
      this.url = prepareBase(baseUrl) + path;
      this.resultType = resultType;
      this.reader = new ObjectMapper().readerFor(resultType);
   }

   private String prepareBase(String baseUrl) {
      String clean = baseUrl.replaceAll("http(s)?://", "");
      return "http://" + clean;
   }

   /**
    * Do an HTTP GET no the endpoint
    *
    * @return The de-serialized response
    * @throws RestCallException If we could not connect to the server or could not read
    *                           the response
    */
   public T get() throws RestCallException {
      return get(DEFAULT_GET_TIMEOUT);
   }

   /**
    * Do an HTTP GET no the endpoint
    *
    * @param timeout The time we want to wait for remote server response (in millis)
    * @return The de-serialized response
    * @throws RestCallException If we could not connect to the server or could not read
    *                           the response
    */
   public T get(long timeout) throws RestCallException {
      // Sends the handshake request and wait for response
      Response response;
      ClientBuilder builder = ResteasyClientBuilder.newBuilder();
      builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);

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
      String output = null;
      try {
         output = response.readEntity(String.class);
         return reader.readValue(output);
      } catch (JsonProcessingException e) {
         logger.warn("Could not convert response into {}: \r\n{}",
                     resultType.getSimpleName(),
                     output,
                     e);
         throw new RestCallException("Could not de-serialize response", e);
      } finally {
         client.close();
      }
   }
}
