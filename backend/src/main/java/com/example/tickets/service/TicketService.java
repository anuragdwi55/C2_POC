package com.example.tickets.service;

import com.example.tickets.domain.Priority;
import com.example.tickets.domain.StatusTransitionRules;
import com.example.tickets.domain.TicketStatus;
import com.example.tickets.domain.exception.TicketNotFoundException;
import com.example.tickets.persistence.CommentEntity;
import com.example.tickets.persistence.Ticket;
import com.example.tickets.persistence.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class TicketService {

    private static final int MAX_PAGE_SIZE = 100;

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket create(String title, String description, Priority priority, String assignee) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(priority);
        ticket.setAssignee(assignee);
        ticket.setStatus(TicketStatus.OPEN);
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> list(TicketStatus status, String q, int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        int safePage = Math.max(page, 0);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "updatedAt"));
        String keyword = q == null ? null : q.trim();
        return ticketRepository.search(status, keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Ticket getById(UUID id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + id));
    }

    @Transactional
    public Ticket update(UUID id, String title, String description, Priority priority, String assignee) {
        Ticket ticket = getById(id);
        if (title != null) {
            ticket.setTitle(title);
        }
        if (description != null) {
            ticket.setDescription(description);
        }
        if (priority != null) {
            ticket.setPriority(priority);
        }
        if (assignee != null) {
            ticket.setAssignee(assignee.isBlank() ? null : assignee);
        }
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket transitionStatus(UUID id, TicketStatus newStatus) {
        Ticket ticket = getById(id);
        StatusTransitionRules.assertTransition(ticket.getStatus(), newStatus);
        ticket.setStatus(newStatus);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public CommentEntity addComment(UUID ticketId, String author, String body) {
        Ticket ticket = getById(ticketId);
        CommentEntity comment = new CommentEntity();
        comment.setAuthor(author);
        comment.setBody(body);
        ticket.addComment(comment);
        ticketRepository.save(ticket);
        return comment;
    }

    @Transactional(readOnly = true)
    public Set<TicketStatus> allowedNextStatuses(UUID id) {
        Ticket ticket = getById(id);
        return StatusTransitionRules.allowedNextStatuses(ticket.getStatus());
    }
}
