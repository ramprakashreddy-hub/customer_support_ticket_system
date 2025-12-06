package com.ticket.ports.out;

import java.util.List;
import java.util.Optional;

import com.ticket.adapters.out.persistence.replica.ReplicaTicketEntity;
import com.ticket.domain.model.TicketStatus;

public interface ReplicaTicketRepositoryPort {
      
    List<ReplicaTicketEntity> findByStatus(TicketStatus status);

    Optional<ReplicaTicketEntity> findById(String ticketId);

    ReplicaTicketEntity save(ReplicaTicketEntity entity);

    List<ReplicaTicketEntity> findByUserId(String userId);
}
