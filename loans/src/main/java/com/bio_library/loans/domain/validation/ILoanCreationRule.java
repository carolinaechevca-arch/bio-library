package com.bio_library.loans.domain.validation;

@FunctionalInterface
public interface ILoanCreationRule {
    void validate(Double gpa, long activeLoans);
}
