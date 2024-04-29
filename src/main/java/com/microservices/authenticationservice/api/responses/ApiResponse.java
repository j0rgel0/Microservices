package com.microservices.authenticationservice.api.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String details;

    public ApiResponse(HttpStatus status, String details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.details = details;
    }

    public ApiResponse(HttpStatus status, String message, String details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.details = details;
    }
}