package com.bio_library.loans.domain.exceptions;

public class LoanNotActiveException extends RuntimeException {
    public LoanNotActiveException(String message) {
        super(message);
    }
}
