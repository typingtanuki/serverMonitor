package com.github.typingtanuki.servermonitor.connectors.teams;

import java.util.ArrayList;
import java.util.List;

public class TeamsContainer {
   private String type = "Container";
   private String style = "default";
   private List<TeamsItem> items = new ArrayList<>();

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getStyle() {
      return style;
   }

   public void setStyle(String style) {
      this.style = style;
   }

   public List<TeamsItem> getItems() {
      return items;
   }

   public void setItems(List<TeamsItem> items) {
      this.items = items;
   }

   public <T extends TeamsItem> T add(T item) {
      items.add(item);
      return item;
   }

   public TeamsContainer styleAttention() {
      style = "attention";
      return this;
   }

   public TeamsContainer styleGood() {
      style = "good";
      return this;
   }
}
