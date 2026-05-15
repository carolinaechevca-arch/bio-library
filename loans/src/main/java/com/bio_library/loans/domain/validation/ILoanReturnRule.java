package com.bio_library.loans.domain.validation;

import com.bio_library.loans.domain.model.Loan;

@FunctionalInterface
public interface ILoanReturnRule {
    void validate(Loan loan, Long studentId);
}
