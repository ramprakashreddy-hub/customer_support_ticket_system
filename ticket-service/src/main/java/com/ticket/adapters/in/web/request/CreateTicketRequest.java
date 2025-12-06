package com.ticket.adapters.in.web.request;

import com.ticket.domain.model.TicketStatus;

public class CreateTicketRequest {
    public String title;
    public String description;
    public TicketStatus status = TicketStatus.OPEN;
}
