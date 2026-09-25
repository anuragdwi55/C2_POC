package com.example.tickets.domain;

import com.example.tickets.domain.exception.InvalidStatusTransitionException;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static com.example.tickets.domain.TicketStatus.CANCELLED;
import static com.example.tickets.domain.TicketStatus.CLOSED;
import static com.example.tickets.domain.TicketStatus.IN_PROGRESS;
import static com.example.tickets.domain.TicketStatus.OPEN;
import static com.example.tickets.domain.TicketStatus.RESOLVED;

public final class StatusTransitionRules {

    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED = Map.of(
            OPEN, EnumSet.of(IN_PROGRESS, CANCELLED),
            IN_PROGRESS, EnumSet.of(RESOLVED, CANCELLED),
            RESOLVED, EnumSet.of(CLOSED),
            CLOSED, EnumSet.noneOf(TicketStatus.class),
            CANCELLED, EnumSet.noneOf(TicketStatus.class)
    );

    private StatusTransitionRules() {
    }

    public static void assertTransition(TicketStatus from, TicketStatus to) {
        if (from == to) {
            return;
        }
        Set<TicketStatus> next = ALLOWED.getOrDefault(from, Set.of());
        if (!next.contains(to)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from " + from + " to " + to);
        }
    }

    public static Set<TicketStatus> allowedNextStatuses(TicketStatus from) {
        return ALLOWED.getOrDefault(from, Set.of());
    }
}
