package com.bio_library.loans.domain.constants;

public final class AuthConstants {

    private AuthConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String AUTHENTICATION_REQUIRED_MESSAGE = "Authentication is required to access this resource.";
    public static final String ACCESS_DENIED_EXCEPTION_MESSAGE = "You do not have permission to access this resource.";
}
