package com.github.typingtanuki.servermonitor;

import java.io.IOException;

/**
 * @author clerc
 * @since 2020/01/24
 */
public class MonitorMain {
    public static void main(String... args){
        ServerMonitor monitor = new ServerMonitor();
        try {
            monitor.loadConfig();
            monitor.startMonitoring();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("Monitor interrupted");
            e.printStackTrace(System.err);
        }catch (IOException e){
            System.err.println("IO error");
            e.printStackTrace(System.err);
        }
    }
}
