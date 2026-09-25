package com.example.tickets.web;

import com.example.tickets.domain.TicketStatus;
import com.example.tickets.persistence.CommentEntity;
import com.example.tickets.persistence.Ticket;
import com.example.tickets.web.dto.CommentResponse;
import com.example.tickets.web.dto.TicketResponse;

import java.util.Comparator;
import java.util.List;

public final class TicketMapper {

    private TicketMapper() {
    }

    public static TicketResponse toResponse(Ticket ticket, boolean includeComments) {
        List<CommentResponse> comments = List.of();
        if (includeComments && ticket.getComments() != null) {
            comments = ticket.getComments().stream()
                    .sorted(Comparator.comparing(CommentEntity::getCreatedAt))
                    .map(TicketMapper::toCommentResponse)
                    .toList();
        }
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getAssignee(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                comments
        );
    }

    public static TicketResponse toSummary(Ticket ticket) {
        return toResponse(ticket, false);
    }

    public static CommentResponse toCommentResponse(CommentEntity comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getAuthor(),
                comment.getBody(),
                comment.getCreatedAt()
        );
    }
}
