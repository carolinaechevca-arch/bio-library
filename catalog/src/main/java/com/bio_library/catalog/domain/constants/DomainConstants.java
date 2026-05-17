package com.bio_library.catalog.domain.constants;

public final class DomainConstants {

    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BOOK_NOT_FOUND = "Book with id '%s' was not found.";
    public static final String BOOK_NOT_FOUND_BY_ISBN = "Book with isbn '%s' was not found.";
    public static final String BOOK_ALREADY_EXISTS = "A book with isbn '%s' already exists.";
    public static final String LOAN_LIMIT_EXCEEDED = "Book has no available licenses (total: %d).";
    public static final String LOAN_COUNT_ALREADY_FULL = "Book already has all licenses available.";

    public static final String LOG_FIND_ALL = "[DB] Finding all books category={} page={} size={}";
    public static final String LOG_FIND_BY_ID = "[DB] Finding book by id={}";
    public static final String LOG_FIND_BY_ISBN = "[DB] Finding book by isbn={}";
    public static final String LOG_SAVE = "[DB] Saving book id={}";
    public static final String LOG_SAVED = "[DB] Book saved with id={}";
    public static final String LOG_DELETE = "[DB] Deleting book id={}";
}
