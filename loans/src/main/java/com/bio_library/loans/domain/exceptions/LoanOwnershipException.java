package com.bio_library.loans.domain.exceptions;

public class LoanOwnershipException extends RuntimeException {
    public LoanOwnershipException(String message) {
        super(message);
    }
}
