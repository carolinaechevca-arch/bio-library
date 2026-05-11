package com.bio_library.university_mock.domain.constants;

public final class UniversityConstants {

    private UniversityConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String STUDENT_NOT_FOUND = "No student found with carnet: %s";

    public static final String LOG_FIND_BY_CARNET = "[UNIVERSITY-DB] Looking up student carnet={} university={}";
    public static final String LOG_STUDENT_NOT_FOUND = "[UNIVERSITY-DB] Student not found with carnet: {}";
    public static final String LOG_FIND_ALL_BY_UNIVERSITY = "[UNIVERSITY-DB] Looking up all students for university: {}";
}
