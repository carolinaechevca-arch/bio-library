package com.bio_library.loans.domain.validation.rules;

import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.exceptions.LoanOwnershipException;
import com.bio_library.loans.domain.model.Loan;
import com.bio_library.loans.domain.validation.ILoanReturnRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Order(1)
public class LoanOwnershipReturnRule implements ILoanReturnRule {

    @Override
    public void validate(Loan loan, Long studentId) {
        Optional.of(loan)
                .filter(l -> l.getStudentId().equals(studentId))
                .orElseThrow(() -> {
                    log.warn("Loan id={} does not belong to studentId={}", loan.getId(), studentId);
                    return new LoanOwnershipException(DomainConstants.LOAN_OWNERSHIP_DENIED);
                });
    }
}
