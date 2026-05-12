package com.bio_library.loans.domain.service;

import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.exceptions.LoanNotFoundException;
import com.bio_library.loans.domain.model.Loan;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class LoanDomainService {

    public Loan prepareLoan(Long studentId, String bookId) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Preparing loan for studentId={} bookId={} endDate={}", studentId, bookId,
                now.plusDays(DomainConstants.LOAN_DURATION_DAYS));
        return Loan.builder()
                .studentId(studentId)
                .bookId(bookId)
                .startDate(now)
                .endDate(now.plusDays(DomainConstants.LOAN_DURATION_DAYS))
                .hasUsed(false)
                .active(true)
                .build();
    }

    public Loan validateLoanExists(Loan loan, Long id) {
        return Optional.ofNullable(loan).orElseThrow(() -> {
            log.warn("Loan with id {} was not found", id);
            return new LoanNotFoundException(String.format(DomainConstants.LOAN_NOT_FOUND, id));
        });
    }

    public Loan markAsUsed(Loan loan) {
        log.info("Marking loan id={} as used", loan.getId());
        return loan.toBuilder().hasUsed(true).build();
    }
}
