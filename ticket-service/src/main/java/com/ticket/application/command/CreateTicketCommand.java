package com.ticket.application.command;

import com.ticket.domain.model.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTicketCommand {
    private final String title;
    private final String userId;
    private final String description;
    private final TicketStatus status;

}
