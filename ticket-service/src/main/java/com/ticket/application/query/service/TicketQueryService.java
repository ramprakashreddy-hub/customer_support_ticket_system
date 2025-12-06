package com.ticket.application.query.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ticket.application.query.GetTicketsByStatusQuery;
import com.ticket.application.query.GetTicketByTicketIdQuery;
import com.ticket.application.query.GetTicketByUserIdQuery;
import com.ticket.application.query.handler.GetTicketsByStatusQueryHandler;
import com.ticket.application.query.handler.GetTicketByTicketIdQueryHandler;
import com.ticket.application.query.handler.GetTicketByUserIdQueryHandler;
import com.ticket.domain.model.Ticket;
import com.ticket.domain.model.TicketStatus;

import java.util.List;

@Service
public class TicketQueryService {

    private static final Logger logger = LoggerFactory.getLogger(TicketQueryService.class);

    private final GetTicketByTicketIdQueryHandler byTicket;
    private final GetTicketsByStatusQueryHandler byStatus;
    private final GetTicketByUserIdQueryHandler byUser;

    public TicketQueryService(GetTicketByTicketIdQueryHandler t, GetTicketsByStatusQueryHandler s, GetTicketByUserIdQueryHandler u) {
        byTicket = t;
        byStatus = s;
        byUser = u;
    }

    public Ticket getByTicketId(String ticketId) {
        logger.info("Fetching ticket by ticket ID: {}", ticketId);
        return byTicket.handle(new GetTicketByTicketIdQuery(ticketId));
    }

    public List<Ticket> getByStatus(TicketStatus status) {
        logger.info("Fetching tickets by status: {}", status);
        return byStatus.handle(new GetTicketsByStatusQuery(status));
    }

    public List<Ticket> getByUserId(String userId) {
        logger.info("Fetching ticket by user ID: {}", userId);
        return byUser.handle(new GetTicketByUserIdQuery(userId));
    }
}
