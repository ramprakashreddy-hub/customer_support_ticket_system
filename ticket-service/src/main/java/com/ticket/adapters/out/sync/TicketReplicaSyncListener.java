package com.ticket.adapters.out.sync;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ticket.adapters.in.web.exception.TicketNotFoundException;
import com.ticket.adapters.out.persistence.primary.TicketEntity;
import com.ticket.adapters.out.persistence.replica.ReplicaTicketEntity;
import com.ticket.ports.out.ReplicaTicketRepositoryPort;

@Component
public class TicketReplicaSyncListener {

    private final ReplicaTicketRepositoryPort replicaTicketRepositoryPort;

    public TicketReplicaSyncListener(
            ReplicaTicketRepositoryPort replicaTicketRepositoryPort

    ) {
        this.replicaTicketRepositoryPort = replicaTicketRepositoryPort;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTicketChanged(TicketChangedEvent event) {
        if ("save".equals(event.getEventType())) {
            handleSaveEvent(event);
        } else if ("update".equals(event.getEventType())) {
            handleUpdateEvent(event);
        } else {
            throw new IllegalArgumentException("Unknown event type: " + event.getEventType());
        }
    }

    private void handleSaveEvent(TicketChangedEvent event) {
        TicketEntity entity = event.getTicketEntity();
        ReplicaTicketEntity replicaEntity = new ReplicaTicketEntity(entity.getTicketId(), entity.getTitle(),entity.getUserId(),
                entity.getDescription(), entity.getStatus(), entity.getCreatedAt(), entity.getUpdatedAt());
        replicaTicketRepositoryPort.save(replicaEntity);
    }

    private void handleUpdateEvent(TicketChangedEvent event) {
        TicketEntity entity = event.getTicketEntity();
        ReplicaTicketEntity replicaEntity = replicaTicketRepositoryPort.findById(entity.getTicketId())
                .orElseThrow(
                        () -> new TicketNotFoundException("Replica not found for ticket: " + entity.getTicketId()));
        replicaEntity.setDescription(entity.getDescription());
        replicaEntity.setCreatedAt(entity.getCreatedAt());
        replicaEntity.setStatus(entity.getStatus());
        replicaTicketRepositoryPort.save(replicaEntity);
    }

}
