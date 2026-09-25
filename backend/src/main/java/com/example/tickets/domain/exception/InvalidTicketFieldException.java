package com.example.tickets.domain.exception;

public class InvalidTicketFieldException extends RuntimeException {

    private final String field;

    public InvalidTicketFieldException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
