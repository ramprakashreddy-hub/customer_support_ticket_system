package com.ticket.application.query.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.application.query.GetTicketsByStatusQuery;
import com.ticket.domain.model.Ticket;
import com.ticket.ports.out.ReplicaTicketRepositoryPort;

import java.util.List;

@Component
public class GetTicketsByStatusQueryHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetTicketsByStatusQueryHandler.class);

    private final ReplicaTicketRepositoryPort replicaTicketRepositoryPort;
    private final TicketMapper mapper;

    public GetTicketsByStatusQueryHandler(ReplicaTicketRepositoryPort replicaTicketRepositoryPort,
            TicketMapper mapper) {
        this.replicaTicketRepositoryPort = replicaTicketRepositoryPort;
        this.mapper = mapper;
    }

    public List<Ticket> handle(GetTicketsByStatusQuery query) {
        logger.info("Handling GetTicketsByStatusQuery for status: {}", query.getStatus());
        List<Ticket> tickets = replicaTicketRepositoryPort.findByStatus(query.getStatus())
                .stream()
                .map(mapper::entityToResponse)
                .toList();
        logger.info("Found {} tickets with status: {}", tickets.size(), query.getStatus());
        return tickets;
    }
}
