package com.ticket.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.ticket.domain.model.TicketStatus;

@Data
@AllArgsConstructor
public class UpdateTicketStatusCommand {
    public final String ticketId;
    public final TicketStatus status;
}
