package com.github.typingtanuki.servermonitor.core;

import org.glassfish.jersey.internal.guava.Lists;

import java.util.LinkedList;
import java.util.List;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.now;

public class History {
    public static final long UNLIMITED = -1L;

    private final long limit;
    private LinkedList<Long> values = new LinkedList<>();
    private LinkedList<String> dates = new LinkedList<>();
    private Long max;

    public History(long limit) {
        super();

        this.limit = limit;
    }

    public void touch(Long newData, int historySize, Long max) {
        values.add(newData);
        dates.add(now());
        while (values.size() > historySize) {
            values.poll();
            dates.poll();
        }
        this.max = max;
    }

    public List<Long> getValues() {
        return Lists.newArrayList(values);
    }

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

    public List<String> getDates() {
        return Lists.newArrayList(dates);
    }

    public String getType() {
        return "history";
    }

    public long getLimit() {
        return limit;
    }
}
