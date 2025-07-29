package com.user.application.port.out;

import java.util.List;
import java.util.Optional;

import com.user.dto.User;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(String id);
}
