package com.ticket.adapters.in.web;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import com.ticket.adapters.in.web.request.CreateTicketRequest;
import com.ticket.adapters.out.persistence.primary.TicketEntity;
import com.ticket.adapters.out.persistence.replica.ReplicaTicketEntity;
import com.ticket.domain.model.Ticket;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface TicketMapper {

    Ticket toDomain(CreateTicketRequest dto);

    TicketEntity toEntity(Ticket ticket);

    Ticket toDomain(TicketEntity entity);

    Ticket entityToResponse(TicketEntity entity);

    Ticket entityToResponse(ReplicaTicketEntity entity);
}
