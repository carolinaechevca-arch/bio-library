package com.bio_library.user.domain.exceptions;

public class InvalidUniversityEmailException extends RuntimeException {
    public InvalidUniversityEmailException(String message) {
        super(message);
    }
}
