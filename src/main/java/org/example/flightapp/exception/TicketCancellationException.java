package org.example.flightapp.exception;

public class TicketCancellationException extends RuntimeException {
    public TicketCancellationException(String message) {
        super(message);
    }
}
