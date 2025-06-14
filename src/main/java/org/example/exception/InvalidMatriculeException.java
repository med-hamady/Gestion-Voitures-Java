package org.example.exception;

public class InvalidMatriculeException extends RuntimeException {
    public InvalidMatriculeException(String message) {
        super(message);
    }
}
