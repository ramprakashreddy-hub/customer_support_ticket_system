package com.ticket.application.query;

import com.ticket.domain.model.TicketStatus;

import lombok.Data;

@Data
public class GetTicketsByStatusQuery {
    public final TicketStatus status;

    public GetTicketsByStatusQuery(TicketStatus s) {
        status = s;
    }
}
