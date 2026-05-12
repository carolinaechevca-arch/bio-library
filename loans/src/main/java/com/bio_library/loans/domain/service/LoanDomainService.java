package com.bio_library.loans.domain.service;

import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.exceptions.LoanBlockedException;
import com.bio_library.loans.domain.exceptions.LoanNotActiveException;
import com.bio_library.loans.domain.exceptions.LoanNotFoundException;
import com.bio_library.loans.domain.exceptions.LoanOwnershipException;
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

    public void validateCanBorrow(Double gpa, long activeLoans) {
        if (gpa != null
                && gpa < DomainConstants.MIN_GPA_FOR_MULTIPLE_LOANS
                && activeLoans >= DomainConstants.MAX_ACTIVE_LOANS_LOW_GPA) {
            log.warn("Loan blocked: gpa={} activeLoans={}", gpa, activeLoans);
            throw new LoanBlockedException(DomainConstants.LOAN_BLOCKED_BY_GPA);
        }
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

    public Loan returnLoan(Loan loan, Long studentId) {
        if (!loan.getStudentId().equals(studentId)) {
            log.warn("Loan id={} does not belong to studentId={}", loan.getId(), studentId);
            throw new LoanOwnershipException(DomainConstants.LOAN_OWNERSHIP_DENIED);
        }
        if (!Boolean.TRUE.equals(loan.getActive())) {
            log.warn("Loan id={} is already returned", loan.getId());
            throw new LoanNotActiveException(String.format(DomainConstants.LOAN_NOT_ACTIVE, loan.getId()));
        }
        log.info("Returning loan id={}", loan.getId());
        return loan.toBuilder()
                .active(false)
                .endDate(LocalDateTime.now())
                .build();
    }
}
