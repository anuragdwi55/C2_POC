package com.example.tickets.domain;

import com.example.tickets.domain.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.example.tickets.domain.TicketStatus.CANCELLED;
import static com.example.tickets.domain.TicketStatus.CLOSED;
import static com.example.tickets.domain.TicketStatus.IN_PROGRESS;
import static com.example.tickets.domain.TicketStatus.OPEN;
import static com.example.tickets.domain.TicketStatus.RESOLVED;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StatusTransitionRulesTest {

    @Test
    void shouldAllowHappyPath() {
        assertThatCode(() -> {
            StatusTransitionRules.assertTransition(OPEN, IN_PROGRESS);
            StatusTransitionRules.assertTransition(IN_PROGRESS, RESOLVED);
            StatusTransitionRules.assertTransition(RESOLVED, CLOSED);
        }).doesNotThrowAnyException();
    }

    @Test
    void shouldAllowCancellationFromOpenAndInProgress() {
        assertThatCode(() -> StatusTransitionRules.assertTransition(OPEN, CANCELLED)).doesNotThrowAnyException();
        assertThatCode(() -> StatusTransitionRules.assertTransition(IN_PROGRESS, CANCELLED)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @EnumSource(value = TicketStatus.class, names = {"OPEN", "RESOLVED", "CANCELLED"})
    void shouldRejectClosedToNonTerminal(TicketStatus target) {
        assertThatThrownBy(() -> StatusTransitionRules.assertTransition(CLOSED, target))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void shouldRejectResolvedToOpen() {
        assertThatThrownBy(() -> StatusTransitionRules.assertTransition(RESOLVED, OPEN))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void shouldRejectCancelledToOpen() {
        assertThatThrownBy(() -> StatusTransitionRules.assertTransition(CANCELLED, OPEN))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void shouldRejectOpenToClosed() {
        assertThatThrownBy(() -> StatusTransitionRules.assertTransition(OPEN, CLOSED))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }
}
