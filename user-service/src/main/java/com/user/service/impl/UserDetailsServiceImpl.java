package com.user.service.impl;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.user.entity.UserEntity;
import com.user.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

        private UserRepository userRepo;

        public UserDetailsServiceImpl(UserRepository userRepo) {
                this.userRepo = userRepo;
        }

        @Override
        public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
                UserEntity user = userRepo.findById(userId)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User not found with id: " + userId));

                return new org.springframework.security.core.userdetails.User(
                                user.getUserId(),
                                user.getPassword(),
                                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        }
}