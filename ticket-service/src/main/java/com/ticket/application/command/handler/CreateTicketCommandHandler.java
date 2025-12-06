package com.ticket.application.command.handler;

import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.adapters.out.persistence.primary.TicketEntity;
import com.ticket.adapters.out.sync.TicketChangedEvent;
import com.ticket.application.command.CreateTicketCommand;
import com.ticket.domain.model.Ticket;
import com.ticket.ports.out.TicketRepositoryPort;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CreateTicketCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CreateTicketCommandHandler.class);

    private final TicketRepositoryPort ticketRepositoryPort;
    private final TicketMapper mapper;
    private final ApplicationEventPublisher publisher;

    public CreateTicketCommandHandler(TicketRepositoryPort ticketRepositoryPort, TicketMapper mapper,
            ApplicationEventPublisher publisher) {
        this.ticketRepositoryPort = ticketRepositoryPort;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    public Ticket handle(CreateTicketCommand cmd) {
        logger.info("Handling CreateTicketCommand: title='{}', description='{}', status='{}'", cmd.getTitle(),
                cmd.getDescription(), cmd.getStatus());
        Ticket domain = new Ticket(cmd.getTitle(),cmd.getUserId(), cmd.getDescription(), cmd.getStatus());
        domain.setTicketId(UUID.randomUUID().toString());
        TicketEntity entity = mapper.toEntity(domain);
        TicketEntity saved = ticketRepositoryPort.save(entity);
        publisher.publishEvent(new TicketChangedEvent("save", saved));
        logger.info("Ticket created with ID: {}", saved.getTicketId());
        return mapper.toDomain(saved);
    }

}
