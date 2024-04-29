package com.microservices.authenticationservice.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final Object resourceId;

    public ResourceNotFoundException(String message, Object resourceId) {
        super(message);
        this.resourceId = resourceId;
    }

}