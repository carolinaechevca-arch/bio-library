package com.bio_library.loans.application.ports.in;

import com.bio_library.loans.domain.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILoanServicePort {
    Loan createLoan(String bookId, Long studentId, Double gpa, String studentEmail);
    Loan markAsUsed(Long loanId);
    Loan returnLoan(Long loanId, Long studentId, String studentEmail);
    Page<Loan> getLoansByStudentId(Long studentId, Boolean active, Pageable pageable);
}
