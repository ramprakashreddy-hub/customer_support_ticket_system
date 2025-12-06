package com.ticket.application.query.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.application.query.GetTicketByUserIdQuery;
import com.ticket.domain.model.Ticket;
import com.ticket.ports.out.ReplicaTicketRepositoryPort;

@Component
public class GetTicketByUserIdQueryHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetTicketByUserIdQueryHandler.class);
    private final ReplicaTicketRepositoryPort replicaTicketRepositoryPort;
    private final TicketMapper mapper;

    public GetTicketByUserIdQueryHandler(ReplicaTicketRepositoryPort replicaTicketRepositoryPort, TicketMapper m) {
        this.replicaTicketRepositoryPort = replicaTicketRepositoryPort;
        mapper = m;
    }

    public List<Ticket> handle(GetTicketByUserIdQuery query) {
        logger.info("Handling GetTicketByUserIdQueryHandler for user ID: {}", query.userId);
        List<Ticket> tickets = replicaTicketRepositoryPort.findByUserId(query.getUserId()).stream()
                .map(mapper::entityToResponse)
                .toList();
        logger.info("Found {} tickets for user ID: {}", tickets.size(),query.getUserId());
        return tickets;
    }
}
