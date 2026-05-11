package com.bio_library.user.domain.exceptions;

public class UniversityStudentNotFoundException extends RuntimeException {
    public UniversityStudentNotFoundException(String message) {
        super(message);
    }
}
