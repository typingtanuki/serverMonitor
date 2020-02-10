package com.github.typingtanuki.servermonitor;

import com.github.typingtanuki.servermonitor.monitors.ServerMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author clerc
 * @since 2020/01/24
 */
public class MonitorMain {
    private static final Logger logger = LoggerFactory.getLogger(MonitorMain.class);
    public static ServerMonitor monitor;

    public static void main(String... args) {
        monitor = new ServerMonitor();
        try {
            monitor.loadConfig();
            monitor.startServer();
            monitor.startMonitoring();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Monitor interrupted", e);
            System.exit(20);
        } catch (IOException e) {
            logger.warn("IO error", e);
            System.exit(21);
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Invalid monitor state", e);
            System.exit(22);
        } catch (RuntimeException e) {
            logger.warn("Unexpected internal error", e);
            System.exit(23);
        }
    }
}
