package org.example.exception;

public class InvalidPrixVenteException extends RuntimeException {
    public InvalidPrixVenteException(String message) {
        super(message);
    }
}