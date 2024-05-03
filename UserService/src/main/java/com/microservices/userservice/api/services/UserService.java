package com.microservices.userservice.api.services;

import com.microservices.userservice.api.models.dto.UserDTO;
import com.microservices.userservice.api.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;



public interface UserService {

    Page<UserDTO> getAllUsers(Pageable pageable, String search);

    UserDTO getUserById(UUID id);

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(UUID id, UserDTO userDTO);

    ApiResponse deleteUser(UUID id);
}