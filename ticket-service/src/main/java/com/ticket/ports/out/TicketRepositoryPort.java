package com.ticket.ports.out;

import java.util.List;
import java.util.Optional;

import com.ticket.adapters.out.persistence.primary.TicketEntity;
import com.ticket.domain.model.TicketStatus;

public interface TicketRepositoryPort {

    TicketEntity save(TicketEntity entity);

    List<TicketEntity> findByStatus(TicketStatus status);

    Optional<TicketEntity> findById(String ticketId);

}
