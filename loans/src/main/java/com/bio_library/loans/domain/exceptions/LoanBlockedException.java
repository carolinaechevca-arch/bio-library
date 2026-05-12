package com.bio_library.loans.domain.exceptions;

public class LoanBlockedException extends RuntimeException {
    public LoanBlockedException(String message) {
        super(message);
    }
}
