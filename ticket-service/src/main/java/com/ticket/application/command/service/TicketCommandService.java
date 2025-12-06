package com.ticket.application.command.service;

import com.ticket.application.command.CreateTicketCommand;
import com.ticket.application.command.UpdateTicketStatusCommand;
import com.ticket.application.command.handler.CreateTicketCommandHandler;
import com.ticket.application.command.handler.UpdateStatusCommandHandler;
import com.ticket.domain.model.Ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketCommandService {
    private static final Logger logger = LoggerFactory.getLogger(TicketCommandService.class);

    private final CreateTicketCommandHandler createHandler;
    private final UpdateStatusCommandHandler updateHandler;

    public TicketCommandService(CreateTicketCommandHandler c, UpdateStatusCommandHandler u) {
        createHandler = c;
        updateHandler = u;
    }

    @Transactional
    public Ticket create(CreateTicketCommand createTicketCommand) {
        logger.info("Creating ticket with title: {}", createTicketCommand.getTitle());
        return createHandler.handle(createTicketCommand);
    }

    @Transactional
    public Ticket updateStatus(UpdateTicketStatusCommand updateTicketStatusCommand) {
        logger.info("Updating ticket status: ticketId='{}", updateTicketStatusCommand.getTicketId());
        return updateHandler.handle(updateTicketStatusCommand);
    }
}
