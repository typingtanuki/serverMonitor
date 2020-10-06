package com.github.typingtanuki.servermonitor.web.status;

import com.github.typingtanuki.servermonitor.report.Status;

/**
 * REST response for status
 *
 * @author clerc
 * @since 2020/02/07
 */
public class StatusResponse {
   private String identity;
   private Status status;

   public StatusResponse() {
      super();
   }

   public StatusResponse(String identity, Status status) {
      super();
      this.identity = identity;

      this.status = status;
   }

   public void setIdentity(String identity) {
      this.identity = identity;
   }

   public void setStatus(Status status) {
      this.status = status;
   }

   public String getIdentity() {
      return identity;
   }

   public Status getStatus() {
      return status;
   }
}
