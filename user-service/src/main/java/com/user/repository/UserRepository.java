package com.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
  
    Optional<UserEntity> findByEmail(String email);
}
