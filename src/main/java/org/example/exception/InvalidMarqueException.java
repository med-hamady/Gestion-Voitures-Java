package org.example.exception;

public class InvalidMarqueException extends RuntimeException {
    public InvalidMarqueException(String message) {
        super(message);
    }
}
