package com.example.tickets.persistence;

import com.example.tickets.domain.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query("""
            SELECT t FROM Ticket t
            WHERE (:status IS NULL OR t.status = :status)
            AND (
                :q IS NULL OR :q = ''
                OR LOWER(t.title) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(t.description) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            """)
    Page<Ticket> search(
            @Param("status") TicketStatus status,
            @Param("q") String q,
            Pageable pageable);
}
