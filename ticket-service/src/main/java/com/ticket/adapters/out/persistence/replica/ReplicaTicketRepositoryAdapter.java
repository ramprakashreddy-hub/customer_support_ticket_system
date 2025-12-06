package com.ticket.adapters.out.persistence.replica;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ticket.domain.model.TicketStatus;
import com.ticket.ports.out.ReplicaTicketRepositoryPort;

@Repository
public class ReplicaTicketRepositoryAdapter implements ReplicaTicketRepositoryPort {

    private final JpaReplicaTicketRepository jpaReplicaTicketRepository;

    public ReplicaTicketRepositoryAdapter(JpaReplicaTicketRepository jpaReplicaTicketRepository) {
        this.jpaReplicaTicketRepository = jpaReplicaTicketRepository;
    }

    @Override
    public List<ReplicaTicketEntity> findByStatus(TicketStatus status) {
        return jpaReplicaTicketRepository.findByStatus(status);
    }

    @Override
    public Optional<ReplicaTicketEntity> findById(String ticketId) {
        return jpaReplicaTicketRepository.findById(ticketId);
    }

    @Override
    public ReplicaTicketEntity save(ReplicaTicketEntity entity) {
        return jpaReplicaTicketRepository.save(entity);
    }

    @Override
    public List<ReplicaTicketEntity> findByUserId(String userId) {
       return jpaReplicaTicketRepository.findByUserId(userId);
    }

}
