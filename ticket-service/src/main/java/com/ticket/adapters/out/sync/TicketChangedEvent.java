package com.ticket.adapters.out.sync;

import com.ticket.adapters.out.persistence.primary.TicketEntity;

import lombok.Data;

@Data
public class TicketChangedEvent {

    private final String eventType;

    private final TicketEntity ticketEntity;

    public TicketChangedEvent(String eventType, TicketEntity ticketEntity) {
        this.eventType = eventType;
        this.ticketEntity = ticketEntity;
    }
}
