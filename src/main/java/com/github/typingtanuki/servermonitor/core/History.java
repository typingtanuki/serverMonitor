package com.github.typingtanuki.servermonitor.core;

import org.glassfish.jersey.internal.guava.Lists;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.now;

/** Keep a list of at most the X latest longs available */
public class History {
   /** The values saved */
   private final Deque<Long> values = new ArrayDeque<>();
   /** The dates corresponding to the values */
   private final Deque<String> dates = new ArrayDeque<>();

   /** The maximum scale for this data (used to scale charts) */
   private long max;

   public History() {
      super();
   }

   /**
    * Put a new entry into the history
    *
    * @param newData     The value to put in the history
    * @param historySize The maximum size of the history
    * @param max         The biggest value which can be recorded
    */
   public void touch(Long newData, int historySize, Long max) {
      values.add(newData);
      dates.add(now());
      while (values.size() > historySize) {
         values.poll();
         dates.poll();
      }
      this.max = max;
   }

   /**
    * Returns a copy of the current values
    */
   public List<Long> getValues() {
      return Lists.newArrayList(values);
   }

   /**
    * Returns a copy of the current dates
    */
   public List<String> getDates() {
      return Lists.newArrayList(dates);
   }

   /**
    * Returns the max.
    * <p>
    * Max can be either hardcoded or the actual biggest value in the history
    */
   public Long getMax() {
      if (max >= 0) {
         return max;
      }
      long m = 0;
      for (Long l : values) {
         m = Math.max(m, l);
      }
      return m;
   }

   public String getType() {
      return "history";
   }
}
