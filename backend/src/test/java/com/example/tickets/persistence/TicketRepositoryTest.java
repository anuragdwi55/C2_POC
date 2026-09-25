package com.example.tickets.persistence;

import com.example.tickets.domain.Priority;
import com.example.tickets.domain.TicketStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void shouldFilterByStatusAndKeyword() {
        Ticket open = new Ticket();
        open.setTitle("Payment failure");
        open.setDescription("Card declined");
        open.setPriority(Priority.HIGH);
        open.setStatus(TicketStatus.OPEN);
        ticketRepository.save(open);

        Ticket closed = new Ticket();
        closed.setTitle("Payment retry");
        closed.setDescription("Resolved");
        closed.setPriority(Priority.LOW);
        closed.setStatus(TicketStatus.CLOSED);
        ticketRepository.save(closed);

        Page<Ticket> byStatus = ticketRepository.search(TicketStatus.OPEN, null, PageRequest.of(0, 10));
        assertThat(byStatus.getTotalElements()).isEqualTo(1);

        Page<Ticket> byKeyword = ticketRepository.search(null, "payment", PageRequest.of(0, 10));
        assertThat(byKeyword.getTotalElements()).isEqualTo(2);
    }
}
