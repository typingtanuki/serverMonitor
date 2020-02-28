package com.github.typingtanuki.servermonitor.monitors;

import org.glassfish.jersey.internal.guava.Lists;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class WithHistory {
    private final LinkedList<Long> history = new LinkedList<>();
    private final LinkedList<String> historyDate = new LinkedList<>();
    private final DateFormat formatter = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

    protected void touch(long newData, long historySize) {
        history.add(newData);
        historyDate.add(formatter.format(new Date()));
        while (history.size() > historySize) {
            history.poll();
            historyDate.poll();
        }
    }

    protected List<Long> getHistory() {
        return Lists.newArrayList(history);
    }

    protected List<String> getHistoryDate() {
        return Lists.newArrayList(historyDate);
    }
}
