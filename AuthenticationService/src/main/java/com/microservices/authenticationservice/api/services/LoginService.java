package com.microservices.authenticationservice.api.services;

import com.microservices.authenticationservice.api.models.dto.JwtResponseDTO;
import com.microservices.authenticationservice.api.models.dto.LoginRequestDTO;

public interface LoginService {
    JwtResponseDTO login(LoginRequestDTO loginRequestDTO);
}