package com.microservices.userservice.api.util;

public final class ApiConstants {

    private ApiConstants() {
        // Private constructor to prevent instantiation
    }

    public static final String API_BASE_URL = "/api";
    public static final String API_VERSION = "v1";

    // Routes for AdministratorProfileController
    public static final String ADMIN_PROFILES_BASE_URL = API_BASE_URL + "/" + API_VERSION + "/admin-profiles";

    // Routes for ManagerProfileController
    public static final String MANAGER_PROFILES_BASE_URL = API_BASE_URL + "/" + API_VERSION + "/manager-profiles";

}