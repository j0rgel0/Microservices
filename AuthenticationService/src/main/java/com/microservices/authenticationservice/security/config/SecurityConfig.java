package com.microservices.authenticationservice.security.config;

import com.microservices.authenticationservice.api.util.ApiConstants;
import com.microservices.authenticationservice.security.filter.JwtTokenVerifier;
import com.microservices.authenticationservice.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))))
                .addFilterAfter(new JwtTokenVerifier(), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(ApiConstants.AUTH_BASE_URL).permitAll()
                        .requestMatchers("/api/**").hasAnyRole("MANAGER", "ADMINISTRATOR")
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}