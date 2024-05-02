package com.microservices.authenticationservice.api.services.implementation;


import com.microservices.authenticationservice.api.models.dto.JwtResponseDTO;
import com.microservices.authenticationservice.api.models.dto.LoginRequestDTO;
import com.microservices.authenticationservice.api.models.entities.UserEntity;
import com.microservices.authenticationservice.api.repositories.UserRepository;
import com.microservices.authenticationservice.api.services.LoginService;
import com.microservices.authenticationservice.security.utils.JwtUtils;
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