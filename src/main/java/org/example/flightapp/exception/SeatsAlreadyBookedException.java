package org.example.flightapp.exception;

public class SeatsAlreadyBookedException extends RuntimeException {
    public SeatsAlreadyBookedException(String message) {
        super(message);
    }
}
