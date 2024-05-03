package com.microservices.userservice.api.exceptions;

public class InvalidEmailException extends RuntimeException {
    private final String email;

    public InvalidEmailException(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}