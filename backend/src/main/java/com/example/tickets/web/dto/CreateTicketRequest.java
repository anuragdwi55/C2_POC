package com.example.tickets.web.dto;

import com.example.tickets.domain.Priority;
import com.example.tickets.domain.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 5000) String description,
        @NotNull Priority priority,
        @Size(max = 100) String assignee
) {
}
