package com.github.typingtanuki.servermonitor.updates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public abstract class LinuxUpdateChecker extends UpdateChecker {
    protected static final Logger logger = LoggerFactory.getLogger(LinuxUpdateChecker.class);

    protected abstract String binaryName();

    protected boolean isGoodOs() {
        return System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("linux");
    }

    protected boolean hasBinary() {
        ProcessBuilder builder = new ProcessBuilder("which", binaryName());
        List<String> out;
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
        return !out.isEmpty();
    }

    protected List<String> runAndReadOutput(ProcessBuilder builder) throws IOException, InterruptedException {
        builder.redirectErrorStream(true);

        Process process = builder.start();
        InputStreamReader isReader = new InputStreamReader(process.getInputStream());
        BufferedReader reader = new BufferedReader(isReader);
        List<String> output = new LinkedList<>();
        while (process.isAlive()) {
            Thread.sleep(100);
            swallowOutput(reader, output);
        }
        Thread.sleep(100);
        swallowOutput(reader, output);
        return output;
    }

    private void swallowOutput(BufferedReader reader, List<String> output) throws IOException {
        String str;
        while ((str = reader.readLine()) != null) {
            if (!str.isBlank()) {
                output.add(str.strip());
            }
        }
    }

    @Override
    protected boolean isAvailable() {
        if (!isGoodOs()) {
            logger.info("Not good os for {}", binaryName());
            return false;
        }
        if (!hasBinary()) {
            logger.info("Binary {} not installed", binaryName());
            return false;
        }
        logger.info("Checking updates for {}", binaryName());
        return true;
    }
}
