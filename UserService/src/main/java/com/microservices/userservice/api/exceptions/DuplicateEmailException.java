package com.microservices.userservice.api.exceptions;

public class DuplicateEmailException extends RuntimeException {
    private final String email;

    public DuplicateEmailException(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}