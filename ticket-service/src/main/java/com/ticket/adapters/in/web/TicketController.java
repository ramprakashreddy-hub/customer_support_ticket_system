package com.ticket.adapters.in.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.adapters.in.web.reposne.ApiResponse;
import com.ticket.adapters.in.web.request.CreateTicketRequest;
import com.ticket.adapters.in.web.request.UpdateStatusRequest;
import com.ticket.application.command.CreateTicketCommand;
import com.ticket.application.command.UpdateTicketStatusCommand;
import com.ticket.application.command.service.TicketCommandService;
import com.ticket.application.query.service.TicketQueryService;
import com.ticket.config.JwtUtil;
import com.ticket.domain.model.Ticket;
import com.ticket.domain.model.TicketStatus;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    private final TicketCommandService cmdService;
    private final TicketQueryService queryService;
    private final JwtUtil jwtUtil;

    public TicketController(TicketCommandService cmdService, TicketQueryService queryService, JwtUtil jwtUtil) {
        this.cmdService = cmdService;
        this.queryService = queryService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Ticket>> create(@RequestBody CreateTicketRequest dto,
            @RequestHeader("Authorization") String authHeader) {
        logger.info("Creating ticket request received: title='{}'", dto.title);

        if (dto.title == null || dto.title.trim().isEmpty()) {
            logger.warn("Ticket creation failed: title is null or empty");
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Title cannot be null or empty"));
        }
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);
        Ticket ticket = cmdService.create(
                new CreateTicketCommand(dto.title, userId, dto.description, dto.status));

        logger.info("Ticket created successfully with ID: {}", ticket.getTicketId());
        return ResponseEntity.ok(new ApiResponse<Ticket>(HttpStatus.OK.value(), "Ticket created successfully", ticket));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Ticket>> updateStatus(@PathVariable String id,
            @RequestBody UpdateStatusRequest dto) {
        logger.info("Updating ticket status: ticketId='{}', newStatus='{}'", id, dto.status);

        if (dto.status == null) {
            logger.warn("Status update failed: status is null");
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Status cannot be null"));
        }

        Ticket updatedTicket = cmdService.updateStatus(new UpdateTicketStatusCommand(id, dto.status));

        logger.info("Ticket ID '{}' status updated to '{}'", id, dto.status);
        return ResponseEntity.ok(
                new ApiResponse<Ticket>(HttpStatus.OK.value(), "Ticket status updated successfully", updatedTicket));
    }

    @GetMapping("/{ticektId}")
    public ResponseEntity<ApiResponse<Ticket>> getByTicketId(@PathVariable String ticektId) {
        logger.info("Fetching ticket with ID: {}", ticektId);
        Ticket ticket = queryService.getByTicketId(ticektId);
        logger.info("Found ticket for ID: {}", ticektId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Fetched Ticket successfully", ticket));
    }

     @GetMapping
    public ResponseEntity<ApiResponse<List<Ticket>>> getByuserID(@RequestParam String userId) {
        logger.info("Fetching tickets with user ID: {}", userId);
        List<Ticket> ticket = queryService.getByUserId(userId);
        logger.info("Found ticket for ID: {}", userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Fetched tickets by user ID successfully", ticket));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Ticket>>> getByStatus(@PathVariable TicketStatus status) {
        logger.info("Fetching tickets with status: {}", status);
        List<Ticket> tickets = queryService.getByStatus(status);
        logger.info("Found {} tickets with status: {}", tickets.size(), status);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Tickets fetched successfully", tickets));
    }
}
