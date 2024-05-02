package com.microservices.authenticationservice.security.service;

import com.microservices.authenticationservice.api.models.entities.UserEntity;
import com.microservices.authenticationservice.api.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        String role = getRole(userEntity.getRole());
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

        return new User(userEntity.getEmail(), userEntity.getPassword(), Collections.singletonList(authority));
    }

    private String getRole(String role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        return switch (role.toUpperCase()) {
            case "ADMINISTRATOR" -> "ROLE_ADMINISTRATOR";
            case "MANAGER" -> "ROLE_MANAGER";
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
}