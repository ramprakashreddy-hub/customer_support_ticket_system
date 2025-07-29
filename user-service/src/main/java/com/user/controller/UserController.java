package com.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.dto.ApiResponse;
import com.user.dto.CreatUserRequest;
import com.user.dto.LoginRequest;
import com.user.dto.User;
import com.user.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody CreatUserRequest req) {
        logger.info("Register request for email: {}", req.getEmail());
        User registeredUser = service.register(req.getName(), req.getEmail(), req.getPassword(), req.getRole());
        return ResponseEntity.ok(new ApiResponse<>(200, "User registered successfully", registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest req) {
        logger.info("Login attempt for email: {}", req.getEmail());
        String token = service.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(new ApiResponse<>(200, "Login successful", token));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<User>> profile(@PathVariable String userId) {
        logger.info("Fetching profile for user ID: {}", userId);
        User user = service.getProfile(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Profile fetched successfully", user));
    }
}
