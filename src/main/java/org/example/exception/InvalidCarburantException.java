package org.example.exception;

public class InvalidCarburantException extends RuntimeException {
    public InvalidCarburantException(String message) {
        super(message);
    }
}