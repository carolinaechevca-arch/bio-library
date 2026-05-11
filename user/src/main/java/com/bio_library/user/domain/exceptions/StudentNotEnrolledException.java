package com.bio_library.user.domain.exceptions;

public class StudentNotEnrolledException extends RuntimeException {
    public StudentNotEnrolledException(String message) {
        super(message);
    }
}
