package com.bio_library.catalog.domain.constants;

public final class DomainConstants {

    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final int MAX_CONCURRENT_LOANS = 5;

    public static final String BOOK_NOT_FOUND = "Book with id %s was not found.";
    public static final String LOAN_LIMIT_EXCEEDED = "Book has reached its maximum concurrent loan limit of %d.";
    public static final String LOAN_COUNT_ALREADY_ZERO = "Book active loan count is already zero.";

    public static final String LOG_FIND_ALL = "[DB] Finding all books page={} size={}";
    public static final String LOG_FIND_BY_ID = "[DB] Finding book by id={}";
    public static final String LOG_SAVE = "[DB] Saving book id={}";
    public static final String LOG_SAVED = "[DB] Book saved with id={}";
}
