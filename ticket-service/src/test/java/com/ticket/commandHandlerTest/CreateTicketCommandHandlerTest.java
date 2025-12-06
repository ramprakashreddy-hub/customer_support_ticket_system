package com.ticket.commandHandlerTest;

import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.adapters.out.persistence.primary.TicketEntity;
import com.ticket.adapters.out.sync.TicketChangedEvent;
import com.ticket.application.command.CreateTicketCommand;
import com.ticket.application.command.handler.CreateTicketCommandHandler;
import com.ticket.domain.model.Ticket;
import com.ticket.domain.model.TicketStatus;
import com.ticket.ports.out.TicketRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateTicketCommandHandlerTest {

    @Mock
    private TicketRepositoryPort repository;

    @Mock
    private TicketMapper mapper;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private CreateTicketCommandHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_shouldCreateAndReturnTicket() {
        CreateTicketCommand cmd = new CreateTicketCommand("title", "user123","desc", TicketStatus.OPEN);
        Ticket domainTicket = new Ticket(cmd.getTitle(), cmd.getUserId(), cmd.getDescription(), cmd.getStatus());
        domainTicket.setTicketId("generated-id");

        TicketEntity entity = new TicketEntity();
        TicketEntity savedEntity = new TicketEntity();
        savedEntity.setTicketId("generated-id");

        when(mapper.toEntity(any(Ticket.class))).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(domainTicket);

        Ticket result = handler.handle(cmd);

        assertNotNull(result);
        assertEquals("generated-id", result.getTicketId());
        verify(publisher).publishEvent(any(TicketChangedEvent.class));
    }
}
