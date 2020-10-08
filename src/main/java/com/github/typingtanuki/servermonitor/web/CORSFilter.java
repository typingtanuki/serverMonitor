package com.github.typingtanuki.servermonitor.web;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Fitler to allow for CORS
 *
 * @author clerc
 * @since 2020/02/13
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

   @Override
   public void filter(ContainerRequestContext request,
                      ContainerResponseContext response) {
      response.getHeaders().add("Access-Control-Allow-Origin", "*");
      response.getHeaders().add("Access-Control-Allow-Headers",
                                "origin, content-type, accept, authorization");
      response.getHeaders().add("Access-Control-Allow-Credentials", "true");
      response.getHeaders().add("Access-Control-Allow-Methods",
                                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
   }
}
