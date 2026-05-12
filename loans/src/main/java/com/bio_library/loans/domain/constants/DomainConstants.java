package com.bio_library.loans.domain.constants;

public final class DomainConstants {

    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final int LOAN_DURATION_DAYS = 10;

    public static final String LOAN_NOT_FOUND = "Loan with id %d was not found.";
    public static final String BOOK_NOT_FOUND = "Book with id %s was not found in the catalog.";
    public static final String BOOK_NOT_AVAILABLE = "Book with id %s has no available copies for loan.";

    public static final double MIN_GPA_FOR_MULTIPLE_LOANS = 3.2;
    public static final int MAX_ACTIVE_LOANS_LOW_GPA = 1;
    public static final String LOAN_BLOCKED_BY_GPA =
            "Loan blocked: students with GPA below 3.2 cannot have more than 1 active loan.";

    public static final String LOAN_NOT_ACTIVE = "Loan with id %d is already returned.";
    public static final String LOAN_OWNERSHIP_DENIED = "You are not authorized to return this loan.";
}
