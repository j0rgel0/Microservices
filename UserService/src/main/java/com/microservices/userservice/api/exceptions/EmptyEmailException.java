package com.microservices.userservice.api.exceptions;

public class EmptyEmailException extends RuntimeException {
    private final String email;

    public EmptyEmailException(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}