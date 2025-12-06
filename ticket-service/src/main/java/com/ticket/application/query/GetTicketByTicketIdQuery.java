package com.ticket.application.query;

import lombok.Data;

@Data
public class GetTicketByTicketIdQuery {
    public final String ticketId;

    public GetTicketByTicketIdQuery(String ticketId) {
        this.ticketId = ticketId;
    }
}
