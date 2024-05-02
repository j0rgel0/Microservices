package com.microservices.authenticationservice.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final Serializable resourceId;

    public ResourceNotFoundException(String message, Serializable resourceId) {
        super(message);
        this.resourceId = resourceId;
    }

}