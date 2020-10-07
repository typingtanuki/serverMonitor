package com.github.typingtanuki.servermonitor.config;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

public class MainConfigTest {
   @Test
   public void defaultInvalid() {
      assertThrows(IllegalStateException.class, () -> new MainConfig().validate());
   }

   @Test
   public void identitySetIsValid() {
      MainConfig config = new MainConfig();
      config.setIdentity("test");
      config.validate();
   }

   @Test
   public void identityNullInvalid() {
      MainConfig config = validBaseConfig();
      config.setIdentity(null);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void identityEmptyInvalid() {
      MainConfig config = validBaseConfig();
      config.setIdentity("");
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void identityBlankInvalid() {
      MainConfig config = validBaseConfig();
      config.setIdentity(" \t ");
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidMonitorTimeUnder() {
      MainConfig config = validBaseConfig();
      config.setMonitorTime(999);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidMonitorTimeOver() {
      MainConfig config = validBaseConfig();
      config.setMonitorTime(3_600_001);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidDebounceTimeUnder() {
      MainConfig config = validBaseConfig();
      config.setMonitorTime(2000);
      config.setDebounceTime(2000);
      config.validate();
      config.setDebounceTime(1999);

      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidDebounceTimeOver() {
      MainConfig config = validBaseConfig();
      config.setMonitorTime(7_200_001);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidPortUnder() {
      MainConfig config = validBaseConfig();
      config.setPort(0);

      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void invalidPortOver() {
      MainConfig config = validBaseConfig();
      config.setPort(65_536);
      assertThrows(IllegalStateException.class, config::validate);
   }

   @Test
   public void copyTo() {
      MainConfig config1 = validBaseConfig();
      config1.setIdentity("test1");
      config1.setMonitorTime(1234);
      config1.setDebounceTime(1235);
      config1.setPort(1236);

      MainConfig config2 = new MainConfig();
      config1.copyTo(config2);

      assertThat(config1.getIdentity(), is(config2.getIdentity()));
      assertThat(config1.getMonitorTime(), is(config2.getMonitorTime()));
      assertThat(config1.getDebounceTime(), is(config2.getDebounceTime()));
      assertThat(config1.getPort(), is(config2.getPort()));
   }


   private static MainConfig validBaseConfig() {
      MainConfig config = new MainConfig();
      config.setIdentity("test");
      config.validate();
      return config;
   }
}