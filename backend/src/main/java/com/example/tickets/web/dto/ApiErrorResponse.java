package com.example.tickets.web.dto;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        List<FieldErrorDto> fieldErrors
) {
    public record FieldErrorDto(String field, String message) {
    }
}
