package com.bio_library.user.domain.constants;

public class StudentConstants {
    public static final String CARNET_REQUIRED_EXCEPTION_MESSAGE = "University carnet is required.";

    public static final String DNI_REQUIRED_EXCEPTION_MESSAGE = "DNI is required.";

    public static final String NAME_REQUIRED_EXCEPTION_MESSAGE = "Name is required.";
    public static final int NAME_MAX_LENGTH = 100;
    public static final String NAME_LENGTH_EXCEPTION_MESSAGE = "Name must not exceed 100 characters.";

    public static final String LAST_NAME_REQUIRED_EXCEPTION_MESSAGE = "Last name is required.";
    public static final int LAST_NAME_MAX_LENGTH = 100;
    public static final String LAST_NAME_LENGTH_EXCEPTION_MESSAGE = "Last name must not exceed 100 characters.";

    public static final String EMAIL_REQUIRED_EXCEPTION_MESSAGE = "Email is required.";
    public static final String EMAIL_INVALID_EXCEPTION_MESSAGE = "Email format is not valid.";
    public static final int EMAIL_MAX_LENGTH = 100;
    public static final String EMAIL_LENGTH_EXCEPTION_MESSAGE = "Email must not exceed 100 characters.";

    public static final String PASSWORD_REQUIRED_EXCEPTION_MESSAGE = "Password is required.";
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final String PASSWORD_LENGTH_EXCEPTION_MESSAGE = "Password must be between 8 and 100 characters.";

    public static final String PHONE_NUMBER_REQUIRED_EXCEPTION_MESSAGE = "Phone number is required.";

    public static final String UNIVERSITY_REQUIRED_EXCEPTION_MESSAGE = "University is required.";
    public static final String UNIVERSITY_INVALID_EXCEPTION_MESSAGE = "The provided university is not valid.";

    public static final String SANCTION_ACTIVE_REQUIRED_MESSAGE = "The 'active' field is required.";
}
