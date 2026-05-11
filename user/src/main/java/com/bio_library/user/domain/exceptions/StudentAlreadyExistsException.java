package com.bio_library.user.domain.exceptions;

public class StudentAlreadyExistsException extends RuntimeException{
    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}
