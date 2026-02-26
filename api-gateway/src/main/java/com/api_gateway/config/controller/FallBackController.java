package com.api_gateway.config.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallBackController {

    @RequestMapping("/ticket-service")
    public ResponseEntity<Map<String, Object>> ticketServiceFallback() {
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now().toString(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Ticket Service is currently unavailable. Please try again later.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @RequestMapping("/user-service")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now().toString(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "User Service is currently unavailable. Please try again later.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @RequestMapping("/admin-service")
    public ResponseEntity<Map<String, Object>> adminServiceFallback() {
        Map<String, Object> response = Map.of(
                "timestamp", Instant.now().toString(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Admin Service is currently unavailable. Please try again later.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

}
