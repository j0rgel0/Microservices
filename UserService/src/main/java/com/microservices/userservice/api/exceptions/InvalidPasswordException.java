package com.microservices.userservice.api.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidPasswordException extends RuntimeException {
    private final String password;
    private final int passwordLength;

    // This constructor allows the creation of the exception with only the necessary detail about the password
    public InvalidPasswordException(String message, String password, int passwordLength) {
        super(message);
        this.password = password;
        this.passwordLength = passwordLength;
    }
}