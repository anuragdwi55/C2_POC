package com.example.tickets.web;

import com.example.tickets.domain.TicketStatus;
import com.example.tickets.service.TicketService;
import com.example.tickets.web.dto.CreateCommentRequest;
import com.example.tickets.web.dto.CreateTicketRequest;
import com.example.tickets.web.dto.PagedTicketResponse;
import com.example.tickets.web.dto.StatusUpdateRequest;
import com.example.tickets.web.dto.TicketResponse;
import com.example.tickets.web.dto.UpdateTicketRequest;
import com.example.tickets.persistence.Ticket;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(@Valid @RequestBody CreateTicketRequest request) {
        Ticket ticket = ticketService.create(
                request.title(),
                request.description(),
                request.priority(),
                request.assignee()
        );
        return TicketMapper.toSummary(ticket);
    }

    @GetMapping
    public PagedTicketResponse list(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<Ticket> result = ticketService.list(status, q, page, size);
        return new PagedTicketResponse(
                result.getContent().stream().map(TicketMapper::toSummary).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements()
        );
    }

    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable UUID id) {
        return TicketMapper.toResponse(ticketService.getById(id), true);
    }

    @GetMapping("/{id}/allowed-transitions")
    public Set<TicketStatus> allowedTransitions(@PathVariable UUID id) {
        return ticketService.allowedNextStatuses(id);
    }

    @PatchMapping("/{id}")
    public TicketResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateTicketRequest request) {
        Ticket ticket = ticketService.update(
                id,
                request.title(),
                request.description(),
                request.priority(),
                request.assignee()
        );
        return TicketMapper.toSummary(ticket);
    }

    @PatchMapping("/{id}/status")
    public TicketResponse updateStatus(@PathVariable UUID id, @Valid @RequestBody StatusUpdateRequest request) {
        Ticket ticket = ticketService.transitionStatus(id, request.status());
        return TicketMapper.toSummary(ticket);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public com.example.tickets.web.dto.CommentResponse addComment(
            @PathVariable UUID id,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        return TicketMapper.toCommentResponse(
                ticketService.addComment(id, request.author(), request.body())
        );
    }
}
