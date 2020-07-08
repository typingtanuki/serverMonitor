package com.github.typingtanuki.servermonitor.connectors.teams;

public class TeamsTextBlock extends TeamsItem {
   private String text = "";
   private String type = "TextBlock";
   private String size = "default";
   private String weight = "default";

   public TeamsTextBlock(String text) {
      this.text = text;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getSize() {
      return size;
   }

   public void setSize(String size) {
      this.size = size;
   }

   public String getWeight() {
      return weight;
   }

   public void setWeight(String weight) {
      this.weight = weight;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public TeamsTextBlock large() {
      size = "large";
      return this;
   }

   public TeamsTextBlock bolder() {
      weight = "bolder";
      return this;
   }
}
