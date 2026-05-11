package com.bio_library.user.domain.constants;

public final class DomainConstants {

    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String STUDENT_EMAIL_ALREADY_EXISTS = "Student with email %s already exists.";
    public static final String STUDENT_DNI_ALREADY_EXISTS = "Student with DNI %s already exists.";
    public static final String STUDENT_NOT_FOUND = "Student with id %d was not found.";
    public static final String INVALID_UNIVERSITY_EMAIL = "Email must belong to the university domain: @%s";
    public static final String UNIVERSITY_STUDENT_NOT_FOUND = "Student with carnet %s was not found in the university system.";
    public static final String UNIVERSITY_DNI_MISMATCH = "The provided DNI does not match the one registered at the university.";
    public static final String UNIVERSITY_EMAIL_MISMATCH = "The provided email does not match the one registered at the university.";
    public static final String STUDENT_NOT_ENROLLED = "The student is not currently enrolled and cannot register.";
}