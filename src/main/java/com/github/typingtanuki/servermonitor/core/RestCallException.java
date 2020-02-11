package com.github.typingtanuki.servermonitor.core;

public class RestCallException extends Exception {
    public RestCallException(String message) {
        super(message);
    }

    public RestCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public String shortMessage() {
        if (getCause() == null) {
            return getMessage();
        }
        return getMessage() + ": " + getCause().getMessage();
    }
}
