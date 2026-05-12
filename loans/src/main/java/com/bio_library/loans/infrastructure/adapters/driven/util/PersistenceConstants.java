package com.bio_library.loans.infrastructure.adapters.driven.util;

public final class PersistenceConstants {

    private PersistenceConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String LOAN_SAVE = "[DB] Saving loan studentId={} bookId={}";
    public static final String LOAN_SAVED = "[DB] Loan saved with id={}";
    public static final String LOAN_FIND_BY_ID = "[DB] Finding loan by id={}";
}
