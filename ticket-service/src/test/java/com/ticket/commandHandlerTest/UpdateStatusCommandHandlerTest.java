package com.ticket.commandHandlerTest;

import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.adapters.in.web.exception.TicketNotFoundException;
import com.ticket.adapters.out.persistence.primary.TicketEntity;
import com.ticket.adapters.out.sync.TicketChangedEvent;
import com.ticket.application.command.UpdateTicketStatusCommand;
import com.ticket.application.command.handler.UpdateStatusCommandHandler;
import com.ticket.domain.model.Ticket;
import com.ticket.domain.model.TicketStatus;
import com.ticket.ports.out.TicketRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateStatusCommandHandlerTest {

    @Mock
    private TicketRepositoryPort repository;

    @Mock
    private TicketMapper mapper;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private UpdateStatusCommandHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_shouldUpdateStatusAndReturnTicket() {
        String ticketId = "ticket-1";
        UpdateTicketStatusCommand cmd = new UpdateTicketStatusCommand(ticketId, TicketStatus.IN_PROGRESS);

        TicketEntity entity = new TicketEntity();
        entity.setTicketId(ticketId);
        entity.setStatus(TicketStatus.OPEN);

        TicketEntity updatedEntity = new TicketEntity();
        updatedEntity.setTicketId(ticketId);
        updatedEntity.setStatus(TicketStatus.IN_PROGRESS);

        Ticket domainTicket = new Ticket("title", "userId", "desc", TicketStatus.IN_PROGRESS);
        domainTicket.setTicketId(ticketId);

        when(repository.findById(ticketId)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(updatedEntity);
        when(mapper.toDomain(updatedEntity)).thenReturn(domainTicket);

        Ticket result = handler.handle(cmd);

        assertNotNull(result);
        assertEquals(TicketStatus.IN_PROGRESS, result.getStatus());
        verify(publisher).publishEvent(any(TicketChangedEvent.class));
    }

    @Test
    void handle_shouldThrowException_whenTicketNotFound() {
        String ticketId = "non-existent";
        UpdateTicketStatusCommand cmd = new UpdateTicketStatusCommand(ticketId, TicketStatus.IN_PROGRESS);

        when(repository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> handler.handle(cmd));
        verify(repository, never()).save(any());
        verify(publisher, never()).publishEvent(any());
    }
}
