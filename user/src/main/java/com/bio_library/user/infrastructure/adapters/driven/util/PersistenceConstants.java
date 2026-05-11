package com.bio_library.user.infrastructure.adapters.driven.util;

public final class PersistenceConstants {

    private PersistenceConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String STUDENT_SAVE_START = "[DB] Saving student with email: {}";
    public static final String STUDENT_SAVE_SUCCESS = "[DB] Student saved with ID: {}";
    public static final String STUDENT_EMAIL_CHECK = "[DB] Checking existence by email: {}";
    public static final String STUDENT_DNI_CHECK = "[DB] Checking existence by DNI: {}";
    public static final String USER_FIND_BY_EMAIL = "[DB] Looking up user by email: {}";
    public static final String USER_FOUND = "[DB] User found: {}";
    public static final String USER_NOT_FOUND = "[DB] User not found: {}";
}
