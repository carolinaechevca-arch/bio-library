package com.bio_library.loans.domain.factory;

import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.model.Loan;

import java.time.LocalDate;

public class LoanFactory {

    private LoanFactory() {}

    public static Loan newLoan(Long studentId, String bookId) {
        LocalDate today = LocalDate.now();
        return Loan.builder()
                .studentId(studentId)
                .bookId(bookId)
                .startDate(today)
                .endDate(today.plusDays(DomainConstants.LOAN_DURATION_DAYS))
                .hasUsed(false)
                .active(true)
                .build();
    }
}
