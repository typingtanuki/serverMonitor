package com.github.typingtanuki.servermonitor.monitors;

import org.glassfish.jersey.internal.guava.Lists;

import java.util.LinkedList;
import java.util.List;

import static com.github.typingtanuki.servermonitor.report.ReportUtils.now;

public abstract class WithHistory {
    private final LinkedList<Long> history = new LinkedList<>();
    private final LinkedList<String> historyDate = new LinkedList<>();

    protected void touch(long newData, long historySize) {
        history.add(newData);
        historyDate.add(now());
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
