package com.ticket.adapters.out.persistence.replica;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket.domain.model.TicketStatus;

public interface JpaReplicaTicketRepository extends JpaRepository<ReplicaTicketEntity, String> {
    
    List<ReplicaTicketEntity> findByStatus(TicketStatus status);

    Optional<ReplicaTicketEntity> findById(String ticketId);

    ReplicaTicketEntity save(ReplicaTicketEntity entity);
    
    List<ReplicaTicketEntity> findByUserId(String userId);
}
