package com.ticket.queryHandlerTest;

import com.ticket.adapters.in.web.TicketMapper;
import com.ticket.adapters.out.persistence.replica.ReplicaTicketEntity;
import com.ticket.application.query.GetTicketsByStatusQuery;
import com.ticket.application.query.handler.GetTicketsByStatusQueryHandler;
import com.ticket.domain.model.Ticket;
import com.ticket.domain.model.TicketStatus;
import com.ticket.ports.out.ReplicaTicketRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTicketsByStatusQueryHandlerTest {

    @Mock
    private ReplicaTicketRepositoryPort replicaRepository;

    @Mock
    private TicketMapper mapper;

    @InjectMocks
    private GetTicketsByStatusQueryHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_shouldReturnListOfTickets_whenFound() {
        TicketStatus status = TicketStatus.OPEN;
        GetTicketsByStatusQuery query = new GetTicketsByStatusQuery(status);

        ReplicaTicketEntity entity1 = new ReplicaTicketEntity();
        entity1.setTicketId("t1");

        ReplicaTicketEntity entity2 = new ReplicaTicketEntity();
        entity2.setTicketId("t2");

        Ticket ticket1 = new Ticket("Title1", "user1", "Desc1", status);
        ticket1.setTicketId("t1");

        Ticket ticket2 = new Ticket("Title2", "user2", "Desc2", status);
        ticket2.setTicketId("t2");

        when(replicaRepository.findByStatus(status)).thenReturn(List.of(entity1, entity2));
        when(mapper.entityToResponse(entity1)).thenReturn(ticket1);
        when(mapper.entityToResponse(entity2)).thenReturn(ticket2);

        List<Ticket> result = handler.handle(query);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("t1", result.get(0).getTicketId());
        assertEquals("t2", result.get(1).getTicketId());

        verify(replicaRepository).findByStatus(status);
        verify(mapper).entityToResponse(entity1);
        verify(mapper).entityToResponse(entity2);
    }

    @Test
    void handle_shouldReturnEmptyList_whenNoTicketsFound() {
        TicketStatus status = TicketStatus.OPEN;
        GetTicketsByStatusQuery query = new GetTicketsByStatusQuery(status);

        when(replicaRepository.findByStatus(status)).thenReturn(List.of());

        List<Ticket> result = handler.handle(query);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(replicaRepository).findByStatus(status);
        verifyNoInteractions(mapper);
    }
}
