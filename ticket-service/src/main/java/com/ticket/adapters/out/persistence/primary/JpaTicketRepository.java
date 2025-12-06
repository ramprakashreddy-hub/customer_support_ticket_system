package com.ticket.adapters.out.persistence.primary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket.domain.model.TicketStatus;

public interface JpaTicketRepository extends JpaRepository<TicketEntity, String> {

    List<TicketEntity> findByStatus(TicketStatus status);

    Optional<TicketEntity> findById(String ticketId);

    TicketEntity save(TicketEntity entity);
}
