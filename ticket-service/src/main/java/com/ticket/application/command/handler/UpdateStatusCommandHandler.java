package com.ticket.application.command.handler;

import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.adapters.in.web.exception.TicketNotFoundException;
import com.ticket.adapters.out.persistence.primary.TicketEntity;
import com.ticket.adapters.out.sync.TicketChangedEvent;
import com.ticket.application.command.UpdateTicketStatusCommand;
import com.ticket.domain.model.Ticket;
import com.ticket.ports.out.TicketRepositoryPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UpdateStatusCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(UpdateStatusCommandHandler.class);

    private final TicketRepositoryPort ticketRepositoryPort;
    private final TicketMapper mapper;
    private final ApplicationEventPublisher publisher;

    public UpdateStatusCommandHandler(TicketRepositoryPort ticketRepositoryPort, TicketMapper m,
            ApplicationEventPublisher pub) {
        this.ticketRepositoryPort = ticketRepositoryPort;
        mapper = m;
        publisher = pub;
    }

    public Ticket handle(UpdateTicketStatusCommand cmd) {
        logger.info("Handling UpdateTicketStatusCommand: ticketId='{}', status='{}'", cmd.getTicketId(),
                cmd.getStatus());
        TicketEntity entity = ticketRepositoryPort.findById(cmd.getTicketId())
                .orElseThrow(() -> new TicketNotFoundException("not found"));
        entity.setStatus(cmd.getStatus());
        TicketEntity updatedEntity = ticketRepositoryPort.save(entity);
        publisher.publishEvent(new TicketChangedEvent("update", entity));
        logger.info("Ticket status updated: ticketId='{}', newStatus='{}'", cmd.getTicketId(), cmd.getStatus());
        return mapper.toDomain(updatedEntity);
    }
}
