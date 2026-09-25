package com.example.tickets.web.dto;

import com.example.tickets.domain.Priority;
import com.example.tickets.domain.TicketStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String title,
        String description,
        Priority priority,
        TicketStatus status,
        String assignee,
        Instant createdAt,
        Instant updatedAt,
        List<CommentResponse> comments
) {
}
