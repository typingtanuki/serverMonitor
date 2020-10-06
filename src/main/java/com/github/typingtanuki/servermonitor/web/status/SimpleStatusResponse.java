package com.github.typingtanuki.servermonitor.web.status;

public class SimpleStatusResponse {
   private String identity;
   private SimpleStatus status;

   public String getIdentity() {
      return identity;
   }

   public void setIdentity(String identity) {
      this.identity = identity;
   }

   public SimpleStatus getStatus() {
      return status;
   }

   public void setStatus(SimpleStatus status) {
      this.status = status;
   }
}
