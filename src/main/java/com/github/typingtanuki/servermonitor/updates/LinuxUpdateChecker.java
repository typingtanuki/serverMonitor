package com.github.typingtanuki.servermonitor.updates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public abstract class LinuxUpdateChecker extends UpdateChecker {
    protected static final Logger logger = LoggerFactory.getLogger(LinuxUpdateChecker.class);

    protected abstract String binaryName();

    protected boolean isGoodOs() {
        return System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("linux");
    }

    protected boolean hasBinary() {
        ProcessBuilder builder = new ProcessBuilder("which", binaryName());
        String out;
        try {
            out = runAndReadOutput(builder);
        } catch (IOException e) {
            logger.warn("Could not check for command {}", binaryName(), e);
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Interrupted while checking for command {}", binaryName(), e);
            return false;
        }
        return !out.isBlank();
    }

    protected String runAndReadOutput(ProcessBuilder builder) throws IOException, InterruptedException {
        Process process = builder.start();

        InputStreamReader isReader = new InputStreamReader(process.getInputStream());
        BufferedReader reader = new BufferedReader(isReader);
        StringBuilder sb = new StringBuilder();
        while (process.isAlive()) {
            Thread.sleep(100);
            swallowOutput(reader, sb);
        }
        Thread.sleep(100);
        swallowOutput(reader, sb);
        return sb.toString();
    }

    private void swallowOutput(BufferedReader reader, StringBuilder sb) throws IOException {
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
    }

    @Override
    protected UpdateChecker tryCheck() {
        if (!isGoodOs()) {
            logger.info("Not good os for {}", binaryName());
            return null;
        }
        if (!hasBinary()) {
            logger.info("Binary {} not installed", binaryName());
            return null;
        }
        logger.info("Checking updates for {}", binaryName());
        return this;
    }
}
