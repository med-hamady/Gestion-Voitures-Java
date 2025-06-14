package org.example.exception;

public class InvalidPrixLocationException extends RuntimeException {
    public InvalidPrixLocationException(String message) {
        super(message);
    }
}

