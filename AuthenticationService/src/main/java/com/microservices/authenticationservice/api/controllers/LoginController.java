package com.microservices.authenticationservice.api.controllers;

import com.microservices.authenticationservice.api.models.dto.JwtResponseDTO;
import com.microservices.authenticationservice.api.models.dto.LoginRequestDTO;
import com.microservices.authenticationservice.api.services.LoginService;
import com.microservices.authenticationservice.api.util.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.AUTH_BASE_URL)
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        JwtResponseDTO jwtResponseDTO = loginService.login(loginRequestDTO);
        return ResponseEntity.ok(jwtResponseDTO);
    }
}