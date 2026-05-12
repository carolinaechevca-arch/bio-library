package com.bio_library.loans.domain.constants;

public final class DomainConstants {

    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final int LOAN_DURATION_DAYS = 10;

    public static final String LOAN_NOT_FOUND = "Loan with id %d was not found.";
    public static final String BOOK_NOT_FOUND = "Book with id %s was not found in the catalog.";
    public static final String BOOK_NOT_AVAILABLE = "Book with id %s has no available copies for loan.";
}
