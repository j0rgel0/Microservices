package com.microservices.authenticationservice.api.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AdministratorProfileDTO {
    private UUID userId;
    private String department;
    private String permissionsLevel;

}