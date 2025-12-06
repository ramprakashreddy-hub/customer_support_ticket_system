package com.ticket.queryHandlerTest;

import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.adapters.in.web.exception.TicketNotFoundException;
import com.ticket.adapters.out.persistence.replica.ReplicaTicketEntity;
import com.ticket.application.query.GetTicketByTicketIdQuery;
import com.ticket.application.query.handler.GetTicketByTicketIdQueryHandler;
import com.ticket.domain.model.Ticket;
import com.ticket.domain.model.TicketStatus;
import com.ticket.ports.out.ReplicaTicketRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTicketByTicketIdQueryHandlerTest {

    @Mock
    private ReplicaTicketRepositoryPort replicaRepository;

    @Mock
    private TicketMapper mapper;

    @InjectMocks
    private GetTicketByTicketIdQueryHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_shouldReturnTicket_whenFound() {
        String ticketId = "ticket-123";
        GetTicketByTicketIdQuery query = new GetTicketByTicketIdQuery(ticketId);

        ReplicaTicketEntity entity = new ReplicaTicketEntity();
        entity.setTicketId(ticketId);

        Ticket expectedTicket = new Ticket("Title", "user123", "Description", TicketStatus.OPEN);
        expectedTicket.setTicketId(ticketId);

        when(replicaRepository.findById(ticketId)).thenReturn(Optional.of(entity));
        when(mapper.entityToResponse(entity)).thenReturn(expectedTicket);

        Ticket result = handler.handle(query);

        assertNotNull(result);
        assertEquals(ticketId, result.getTicketId());
        verify(replicaRepository).findById(ticketId);
        verify(mapper).entityToResponse(entity);
    }

    @Test
    void handle_shouldThrowException_whenTicketNotFound() {
        String ticketId = "non-existent";
        GetTicketByTicketIdQuery query = new GetTicketByTicketIdQuery(ticketId);

        when(replicaRepository.findById(ticketId)).thenReturn(Optional.empty());

        TicketNotFoundException exception = assertThrows(TicketNotFoundException.class, () -> handler.handle(query));
        assertEquals("Ticket not found for user ID: " + ticketId, exception.getMessage());

        verify(replicaRepository).findById(ticketId);
        verifyNoInteractions(mapper);
    }
}
