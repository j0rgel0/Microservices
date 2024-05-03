package com.microservices.userservice.api.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdate;
    private boolean softDelete;
    private String role;

}