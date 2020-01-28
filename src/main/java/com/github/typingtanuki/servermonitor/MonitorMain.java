package com.github.typingtanuki.servermonitor;

import com.github.typingtanuki.servermonitor.monitors.ServerMonitor;

import java.io.IOException;

/**
 * @author clerc
 * @since 2020/01/24
 */
public class MonitorMain {
    public static void main(String... args) {
        ServerMonitor monitor = new ServerMonitor();
        try {
            monitor.loadConfig();
            monitor.startMonitoring();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Monitor interrupted");
            e.printStackTrace(System.err);
            System.exit(20);
        } catch (IOException e) {
            System.err.println("IO error");
            e.printStackTrace(System.err);
            System.exit(21);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Invalid monitor state");
            e.printStackTrace(System.err);
            System.exit(22);
        } catch (RuntimeException e) {
            System.err.println("Unexpected internal error");
            e.printStackTrace(System.err);
            System.exit(23);
        }
    }
}
