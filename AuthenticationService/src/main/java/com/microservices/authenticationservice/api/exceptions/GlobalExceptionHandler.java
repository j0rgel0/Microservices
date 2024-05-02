package com.microservices.authenticationservice.api.exceptions;

import com.microservices.authenticationservice.api.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private static final String RESOURCE_NOT_FOUND_MSG = "resource.not.found";
    private static final String EMAIL_REQUIRED_MSG = "email.required";
    private static final String INVALID_EMAIL_MSG = "invalid.email";
    private static final String EMAIL_EXISTS_MSG = "email.exists";
    private static final String PASSWORD_REQUIRED_MSG = "password.required";
    private static final String PASSWORD_INVALID_MSG = "password.invalid";
    private static final String INVALID_UUID_MSG = "invalid.uuid";
    private static final String INTERNAL_SERVER_ERROR_MSG = "internal.server.error";

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private ResponseEntity<ApiResponse> buildResponseEntity(HttpStatus status, String message) {
        ApiResponse response = new ApiResponse(status, message);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        String message = messageSource.getMessage(RESOURCE_NOT_FOUND_MSG, new Object[]{ex.getResourceId()}, LocaleContextHolder.getLocale());
        return buildResponseEntity(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiResponse> handleInvalidEmailException(InvalidEmailException ex) {
        String message = messageSource.getMessage(INVALID_EMAIL_MSG, new Object[]{ex.getEmail()}, LocaleContextHolder.getLocale());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(EmptyEmailException.class)
    public ResponseEntity<ApiResponse> handleEmptyEmailException() {
        String message = messageSource.getMessage(EMAIL_REQUIRED_MSG, null, LocaleContextHolder.getLocale());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse> handleDuplicateEmailException(DuplicateEmailException ex) {
        String message = messageSource.getMessage(EMAIL_EXISTS_MSG, new Object[]{ex.getEmail()}, LocaleContextHolder.getLocale());
        return buildResponseEntity(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(EmptyPasswordException.class)
    public ResponseEntity<ApiResponse> handleEmptyPasswordException() {
        String message = messageSource.getMessage(PASSWORD_REQUIRED_MSG, null, LocaleContextHolder.getLocale());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse> handleInvalidPasswordException(InvalidPasswordException ex) {
        String message = messageSource.getMessage(PASSWORD_INVALID_MSG, new Object[]{ex.getPassword().length(), 8, 20}, LocaleContextHolder.getLocale());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatchException() {
        String message = messageSource.getMessage(INVALID_UUID_MSG, null, LocaleContextHolder.getLocale());
        log.warn(message);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String details = "The request body is invalid or malformed.";
        log.warn("Invalid request body: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
        String message = messageSource.getMessage(INTERNAL_SERVER_ERROR_MSG, null, LocaleContextHolder.getLocale());
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied warning: {}", ex.getMessage());
        String message = "Access is denied";
        return buildResponseEntity(HttpStatus.FORBIDDEN, message);
    }
}