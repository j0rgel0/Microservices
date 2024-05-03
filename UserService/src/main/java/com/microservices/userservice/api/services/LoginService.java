package com.microservices.userservice.api.services;

import com.microservices.userservice.api.models.dto.JwtResponseDTO;
import com.microservices.userservice.api.models.dto.LoginRequestDTO;

public interface LoginService {
    JwtResponseDTO login(LoginRequestDTO loginRequestDTO);
}