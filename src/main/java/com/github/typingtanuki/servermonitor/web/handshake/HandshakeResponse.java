package com.github.typingtanuki.servermonitor.web.handshake;

public class HandshakeResponse {
   private long requestTime;
   private long responseTime;

   @Deprecated
   public HandshakeResponse() {
      super();
   }

   public HandshakeResponse(long requestTime, long responseTime) {
      super();

      this.requestTime = requestTime;
      this.responseTime = responseTime;
   }

   public long getRequestTime() {
      return requestTime;
   }

   public void setRequestTime(long requestTime) {
      this.requestTime = requestTime;
   }

   public long getResponseTime() {
      return responseTime;
   }

   public void setResponseTime(long responseTime) {
      this.responseTime = responseTime;
   }
}
