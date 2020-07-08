package com.github.typingtanuki.servermonitor.web.handshake;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/handshake")
public class HandshakeEndpoint {
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public HandshakeResponse shake(
         @QueryParam("request") String requestTime) {
      return new HandshakeResponse(
            Long.parseLong(requestTime),
            System.currentTimeMillis());
   }
}
