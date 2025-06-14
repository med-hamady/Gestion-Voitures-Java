package org.example.exception;

public class InvalidTelephoneException extends RuntimeException {
    public InvalidTelephoneException(String message) {
        super(message);
    }
}


