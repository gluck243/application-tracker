package com.gluck.jobtracker.exception;

public class NoSuchJobFoundException extends RuntimeException {
    public NoSuchJobFoundException() {
        super("No matching job found!");
    }

    public NoSuchJobFoundException(String message) {
        super(message);
    }
}

