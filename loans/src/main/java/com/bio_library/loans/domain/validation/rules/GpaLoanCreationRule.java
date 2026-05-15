package com.bio_library.loans.domain.validation.rules;

import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.exceptions.LoanBlockedException;
import com.bio_library.loans.domain.validation.ILoanCreationRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Order(1)
public class GpaLoanCreationRule implements ILoanCreationRule {

    @Override
    public void validate(Double gpa, long activeLoans) {
        Optional.ofNullable(gpa)
                .filter(g -> g < DomainConstants.MIN_GPA_FOR_MULTIPLE_LOANS
                        && activeLoans >= DomainConstants.MAX_ACTIVE_LOANS_LOW_GPA)
                .ifPresent(g -> {
                    log.warn("Loan blocked: gpa={} activeLoans={}", g, activeLoans);
                    throw new LoanBlockedException(DomainConstants.LOAN_BLOCKED_BY_GPA);
                });
    }
}
