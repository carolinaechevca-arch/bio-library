package com.bio_library.loans.domain.validation.rules;

import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.exceptions.LoanNotActiveException;
import com.bio_library.loans.domain.model.Loan;
import com.bio_library.loans.domain.validation.ILoanReturnRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Order(2)
public class LoanActiveStateReturnRule implements ILoanReturnRule {

    @Override
    public void validate(Loan loan, Long studentId) {
        Optional.of(loan)
                .filter(l -> Boolean.TRUE.equals(l.getActive()))
                .orElseThrow(() -> {
                    log.warn("Loan id={} is already returned", loan.getId());
                    return new LoanNotActiveException(
                            String.format(DomainConstants.LOAN_NOT_ACTIVE, loan.getId()));
                });
    }
}
