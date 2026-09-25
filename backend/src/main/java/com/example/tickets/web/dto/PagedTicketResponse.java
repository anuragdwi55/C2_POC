package com.example.tickets.web.dto;

import java.util.List;

public record PagedTicketResponse(
        List<TicketResponse> content,
        int page,
        int size,
        long totalElements
) {
}
