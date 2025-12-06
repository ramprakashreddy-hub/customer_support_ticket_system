package com.ticket.application.query.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.adapters.in.web.exception.TicketNotFoundException;
import com.ticket.adapters.out.persistence.replica.ReplicaTicketEntity;
import com.ticket.application.query.GetTicketByTicketIdQuery;
import com.ticket.domain.model.Ticket;
import com.ticket.ports.out.ReplicaTicketRepositoryPort;

@Component
public class GetTicketByTicketIdQueryHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetTicketByTicketIdQueryHandler.class);
    private final ReplicaTicketRepositoryPort replicaTicketRepositoryPort;
    private final TicketMapper mapper;

    public GetTicketByTicketIdQueryHandler(ReplicaTicketRepositoryPort replicaTicketRepositoryPort,
            TicketMapper mapper) {
        this.replicaTicketRepositoryPort = replicaTicketRepositoryPort;
        this.mapper = mapper;
    }

    public Ticket handle(GetTicketByTicketIdQuery query) {
        logger.info("Handling GetTicketByTicketIdQuery for ticket ID: {}", query.ticketId);
        ReplicaTicketEntity replicaTicketEntity = replicaTicketRepositoryPort.findById(query.ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found for user ID: " + query.ticketId));
        logger.info("Found ticket: {}", replicaTicketEntity);
        return mapper.entityToResponse(replicaTicketEntity);
    }
}
