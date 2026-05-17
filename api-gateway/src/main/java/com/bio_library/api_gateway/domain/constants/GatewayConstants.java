package com.bio_library.api_gateway.domain.constants;

import java.util.List;

public final class GatewayConstants {

    private GatewayConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth",
            "/api/v1/students/create",
            "/actuator"
    );

    public static final List<String> BLOCKED_PATHS = List.of(
            "/api/v1/internal"
    );

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String HEADER_USER_ID    = "X-User-Id";
    public static final String HEADER_USER_EMAIL = "X-User-Email";
    public static final String HEADER_USER_ROLE  = "X-User-Role";
    public static final String HEADER_USER_GPA   = "X-User-Gpa";
}
