package com.bio_library.loans.application.ports.in;

import com.bio_library.loans.domain.model.Loan;

public interface ILoanServicePort {
    Loan createLoan(String bookId, Long studentId, Double gpa);
    Loan markAsUsed(Long loanId);
    Loan returnLoan(Long loanId, Long studentId);
}
