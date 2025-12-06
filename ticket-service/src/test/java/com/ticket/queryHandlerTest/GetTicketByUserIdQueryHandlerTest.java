package com.ticket.queryHandlerTest;


import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.adapters.out.persistence.replica.ReplicaTicketEntity;
import com.ticket.application.query.GetTicketByUserIdQuery;
import com.ticket.application.query.handler.GetTicketByUserIdQueryHandler;
import com.ticket.domain.model.Ticket;
import com.ticket.domain.model.TicketStatus;
import com.ticket.ports.out.ReplicaTicketRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTicketByUserIdQueryHandlerTest {

    @Mock
    private ReplicaTicketRepositoryPort replicaRepository;

    @Mock
    private TicketMapper mapper;

    @InjectMocks
    private GetTicketByUserIdQueryHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_shouldReturnListOfTickets_whenFound() {
        String userId = "user123";
        GetTicketByUserIdQuery query = new GetTicketByUserIdQuery(userId);

        ReplicaTicketEntity entity1 = new ReplicaTicketEntity();
        entity1.setTicketId("ticket-1");

        ReplicaTicketEntity entity2 = new ReplicaTicketEntity();
        entity2.setTicketId("ticket-2");

        Ticket ticket1 = new Ticket("Title 1", userId, "Desc 1", TicketStatus.OPEN);
        ticket1.setTicketId("ticket-1");

        Ticket ticket2 = new Ticket("Title 2", userId, "Desc 2", TicketStatus.IN_PROGRESS);
        ticket2.setTicketId("ticket-2");

        when(replicaRepository.findByUserId(userId)).thenReturn(List.of(entity1, entity2));
        when(mapper.entityToResponse(entity1)).thenReturn(ticket1);
        when(mapper.entityToResponse(entity2)).thenReturn(ticket2);

        List<Ticket> result = handler.handle(query);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ticket-1", result.get(0).getTicketId());
        assertEquals("ticket-2", result.get(1).getTicketId());

        verify(replicaRepository).findByUserId(userId);
        verify(mapper).entityToResponse(entity1);
        verify(mapper).entityToResponse(entity2);
    }

    @Test
    void handle_shouldReturnEmptyList_whenNoTicketsFound() {
        String userId = "empty-user";
        GetTicketByUserIdQuery query = new GetTicketByUserIdQuery(userId);

        when(replicaRepository.findByUserId(userId)).thenReturn(List.of());

        List<Ticket> result = handler.handle(query);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(replicaRepository).findByUserId(userId);
        verifyNoInteractions(mapper);
    }
}
