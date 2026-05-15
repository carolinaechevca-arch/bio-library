package com.bio_library.loans.domain.service;

import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.exceptions.LoanNotFoundException;
import com.bio_library.loans.domain.model.Loan;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class LoanDomainService {

    public Loan validateLoanExists(Loan loan, Long id) {
        return Optional.ofNullable(loan)
                .orElseThrow(() -> {
                    log.warn("Loan with id {} was not found", id);
                    return new LoanNotFoundException(String.format(DomainConstants.LOAN_NOT_FOUND, id));
                });
    }
}
