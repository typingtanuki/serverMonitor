package com.github.typingtanuki.servermonitor.utils;

import java.util.HashSet;
import java.util.Set;

public final class SimpleStack {
    private SimpleStack() {
        super();
    }

    public static String simpleStack(Throwable e) {
        Set<Throwable> visited = new HashSet<>();
        return simpleStack(visited, e);
    }

    private static String simpleStack(Set<Throwable> visited, Throwable e) {
        if (visited.contains(e)) {
            return "";
        }
        visited.add(e);

        StringBuilder out = new StringBuilder();
        out.append("Caused by: ").append(e.getClass().getSimpleName()).append(": ").append(e.getMessage());
        formatStack(out, e.getStackTrace());

        if (e.getCause() != null && !visited.contains(e.getCause())) {
            out.append(simpleStack(visited, e.getCause()));
        }
        out.append("\r\n");
        return out.toString();
    }

    private static void formatStack(StringBuilder out, StackTraceElement[] stackTrace) {
        for (StackTraceElement element : stackTrace) {
            if (!element.getClassName().contains("typingtanuki")) {
                continue;
            }
            out.append("\tat ").append(element.getClassName()).append(".").append(element.getMethodName())
                    .append("(").append(element.getFileName()).append(":").append(element.getLineNumber()).append(")")
                    .append("\r\n");
        }
    }
}
