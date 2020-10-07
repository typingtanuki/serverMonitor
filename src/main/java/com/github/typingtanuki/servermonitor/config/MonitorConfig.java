package com.github.typingtanuki.servermonitor.config;

/** Base monitor configuration */
public abstract class MonitorConfig {
   /** If false, this monitor will not be executed */
   private boolean enabled = true;

   protected MonitorConfig() {
      super();
   }

   public boolean isEnabled() {
      return enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public abstract void validate();

   protected void innerCopyTo(MonitorConfig targetConfig) {
      targetConfig.enabled = enabled;
   }
}
