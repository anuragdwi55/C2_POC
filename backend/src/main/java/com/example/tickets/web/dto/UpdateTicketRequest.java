package com.example.tickets.web.dto;

import com.example.tickets.domain.Priority;
import jakarta.validation.constraints.Size;

public record UpdateTicketRequest(
        @Size(max = 200) String title,
        @Size(max = 5000) String description,
        Priority priority,
        @Size(max = 100) String assignee
) {
}
