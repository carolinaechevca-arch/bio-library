package com.bio_library.loans.domain.factory;

import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.model.Loan;

import java.time.LocalDateTime;

public class LoanFactory {

    private LoanFactory() {}

    public static Loan newLoan(Long studentId, String bookId) {
        LocalDateTime now = LocalDateTime.now();
        return Loan.builder()
                .studentId(studentId)
                .bookId(bookId)
                .startDate(now)
                .endDate(now.plusDays(DomainConstants.LOAN_DURATION_DAYS))
                .hasUsed(false)
                .active(true)
                .build();
    }
}
