package com.microservices.authenticationservice.api.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ManagerProfileDTO {
    private UUID userId;
    private Integer teamSize;
    private String areaOfResponsibility;

}