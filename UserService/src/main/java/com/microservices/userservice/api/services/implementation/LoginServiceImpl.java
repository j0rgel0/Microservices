package com.microservices.userservice.api.services.implementation;


import com.microservices.userservice.api.models.dto.JwtResponseDTO;
import com.microservices.userservice.api.models.dto.LoginRequestDTO;
import com.microservices.userservice.api.models.entities.UserEntity;
import com.microservices.userservice.api.repositories.UserRepository;
import com.microservices.userservice.api.services.LoginService;
import com.microservices.userservice.security.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public LoginServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    public JwtResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        UserEntity userEntity = userRepository.findByEmail(loginRequestDTO.getEmail());
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }

        String role = userEntity.getRole();

        return new JwtResponseDTO(jwt, "Bearer", role);
    }
}