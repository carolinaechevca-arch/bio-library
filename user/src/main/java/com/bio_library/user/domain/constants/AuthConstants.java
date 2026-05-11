package com.bio_library.user.domain.constants;

public class AuthConstants {
    public static final String INVALID_CREDENTIALS_EXCEPTION_MESSAGE = "Invalid email or password";
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int EMAIL_MAX_LENGTH = 200;


    public static final String EMAIL_REQUIRED_EXCEPTION_MESSAGE = "Email is required";
    public static final String EMAIL_INVALID_EXCEPTION_MESSAGE = "Invalid email format";
    public static final String EMAIL_LENGTH_EXCEPTION_MESSAGE = "Email cannot exceed " + EMAIL_MAX_LENGTH + " characters";

    public static final String PASSWORD_REQUIRED_EXCEPTION_MESSAGE = "Password is required";
    public static final String PASSWORD_LENGTH_EXCEPTION_MESSAGE = "Password must be between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH + " characters long";

    public static final String ACCESS_DENIED_EXCEPTION_MESSAGE = "You do not have the necessary permissions to perform this action.";
    public static final String AUTHENTICATION_REQUIRED_MESSAGE = "Authentication is required to access this resource.";
}
