package com.ticket.adapters.out.persistence.primary;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ticket.domain.model.TicketStatus;
import com.ticket.ports.out.TicketRepositoryPort;

@Repository
public class TicketRepositoryAdapter implements TicketRepositoryPort {

    private final JpaTicketRepository jpaTicketRepository;

    public TicketRepositoryAdapter(JpaTicketRepository jpaTicketRepository) {
        this.jpaTicketRepository = jpaTicketRepository;
    }

    @Override
    public TicketEntity save(TicketEntity entity) {
        return jpaTicketRepository.save(entity);
    }

    @Override
    public List<TicketEntity> findByStatus(TicketStatus status) {
        return jpaTicketRepository.findByStatus(status);
    }

    @Override
    public Optional<TicketEntity> findById(String ticketId) {
        return jpaTicketRepository.findById(ticketId);
    }

}
