package com.user.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.dto.ApiResponse;
import com.user.dto.User;
import com.user.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> allUsers() {
        logger.info("Fetching all users");
        List<User> users = service.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Fetched lal users successfully", users));
    }

    @PatchMapping("/users")
    public ResponseEntity<ApiResponse<String>> updateRole(@RequestParam("id") String userId,@RequestParam("role") String role) {
        logger.info("Updating role for user with ID: {}", userId);
        service.updateUserRole(userId, role);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Role updated successfully"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable("id") String userId) {
        logger.info("Deleting user with ID: {}", userId);
        service.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Deleted User Successfully"));
    }
}