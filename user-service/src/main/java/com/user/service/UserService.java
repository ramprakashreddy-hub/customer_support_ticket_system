package com.user.service;

import java.util.List;

import com.user.dto.User;

public interface UserService {

    User register(String name, String email, String password, String role);

    String login(String email, String password);

    User getProfile(String userId);

    List<User> getAllUsers();

    void updateUserRole(String userId, String role);

    void deleteUser(String userId);
}
