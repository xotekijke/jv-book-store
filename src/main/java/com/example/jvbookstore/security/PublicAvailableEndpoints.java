package com.example.jvbookstore.security;

import java.util.List;

public class PublicAvailableEndpoints {
    private static final List<String> PUBLIC_ENDPOINT_PREFIXES = List.of(
            "/api/auth/login",
            "/api/auth/registration",
            "/api/v3/api-docs",
            "/api/swagger-ui",
            "/api/swagger-resources",
            "/api/webjars"
    );

    public static boolean isPublicEndpoint(String url) {
        return PUBLIC_ENDPOINT_PREFIXES.stream().anyMatch(url::startsWith);
    }
}
