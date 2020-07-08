package com.github.typingtanuki.servermonitor.connectors.teams;

import java.util.LinkedList;
import java.util.List;

/**
 * A group of related facts to be presented together
 */
@SuppressWarnings("unused")
public class TeamsSection {
   private final List<TeamsFact> facts = new LinkedList<>();
   private String activityTitle;
   private String activitySubtitle;
   private String activityImage;
   private boolean markdown = true;

   public String getActivityTitle() {
      return activityTitle;
   }

   public void setActivityTitle(String activityTitle) {
      this.activityTitle = activityTitle;
   }

   public String getActivitySubtitle() {
      return activitySubtitle;
   }

   public void setActivitySubtitle(String activitySubtitle) {
      this.activitySubtitle = activitySubtitle;
   }

   public String getActivityImage() {
      return activityImage;
   }

   public void setActivityImage(String activityImage) {
      this.activityImage = activityImage;
   }

   public List<TeamsFact> getFacts() {
      return facts;
   }

   public boolean isMarkdown() {
      return markdown;
   }

   public void setMarkdown(boolean markdown) {
      this.markdown = markdown;
   }

   public void addFact(TeamsFact fact) {
      facts.add(fact);
   }
}
