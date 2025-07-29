package com.user.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.entity.UserEntity;
import com.user.config.JWTUtils;
import com.user.dto.Role;
import com.user.dto.User;
import com.user.dto.UserMapper;
import com.user.exception.EmailAlreadyExistException;
import com.user.exception.EmailNotFoundException;
import com.user.exception.UserNotFoundException;
import com.user.repository.UserRepository;
import com.user.service.UserService;

@Service

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JWTUtils jwtUtils;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder encoder, JWTUtils jwtUtils, UserMapper mapper) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.mapper = mapper;
    }

    @Override
    public User register(String name, String email, String password, String role) {
        logger.info("Registering user with email: {}", email);

        // Check if email already exists
        Optional<UserEntity> existingUser = userRepo.findByEmail(email);
        if (existingUser.isPresent()) {
            logger.warn("Attempted to register with existing email: {}", email);
            throw new EmailAlreadyExistException("Email is already in use.");
        }

        String validRoleName = getAvailableRole(role);
        String encoded = encoder.encode(password);

        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID().toString());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encoded);
        user.setRole(validRoleName);

        UserEntity userEntity = userRepo.save(user);
        logger.info("User registered successfully with ID: {}", userEntity.getUserId());

        return mapper.toDomain(userEntity);
    }

    @Override
    public String login(String email, String password) {
        logger.info("User login attempt with email: {}", email);
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("In Correct Email Id"));
        if (!encoder.matches(password, user.getPassword())) {
            logger.error("Invalid credentials for email: {}", email);
            throw new RuntimeException("Invalid credentials");
        }
        logger.info("User logged in successfully with ID: {}", user.getUserId());
        return jwtUtils.generateToken(user);
    }

    @Override
    public User getProfile(String userId) {
        logger.info("Fetching profile for user ID: {}", userId);
        UserEntity entity = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        logger.info("User profile fetched successfully for ID: {}", userId);
        return mapper.toDomain(entity);

    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<UserEntity> userEntities = userRepo.findAll();
        if (userEntities.isEmpty()) {
            logger.warn("No users found in the system");
            return List.of();
        }
        return mapper.toDomainList(userEntities);
    }

    @Override
    public void updateUserRole(String userId, String role) {
        logger.info("Updating role for user ID: {}", userId);
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        user.setRole(getAvailableRole(role));
        userRepo.save(user);
        logger.info("User role updated successfully for ID: {}", userId);
    }

    @Override
    public void deleteUser(String userId) {
        logger.info("Deleting user with ID: {}", userId);
        userRepo.deleteById(userId);
    }

    public String getAvailableRole(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r.name();
            }
        }
        throw new IllegalArgumentException(
                "Role '" + role + "' is not defined. Allowed roles: ADMIN, CUSTOMER, SUPPORT.");
    }

}
