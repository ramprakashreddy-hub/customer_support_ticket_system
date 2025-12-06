package com.ticket.application.query;

import lombok.Data;

@Data
public class GetTicketByUserIdQuery {
    public final String userId;

    public GetTicketByUserIdQuery(String userId) {
        this.userId = userId;
    }
}
