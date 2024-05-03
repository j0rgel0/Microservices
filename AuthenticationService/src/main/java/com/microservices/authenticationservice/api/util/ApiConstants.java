package com.microservices.authenticationservice.api.util;

public final class ApiConstants {

    private ApiConstants() {
        // Private constructor to prevent instantiation
    }

    public static final String API_BASE_URL = "/api";
    public static final String API_VERSION = "v1";

    // Routes for Login
    public static final String AUTH_BASE_URL = API_BASE_URL + "/" + API_VERSION + "/auth";
    public static final String AUTH_LOGIN_URL = AUTH_BASE_URL + "/login";

}