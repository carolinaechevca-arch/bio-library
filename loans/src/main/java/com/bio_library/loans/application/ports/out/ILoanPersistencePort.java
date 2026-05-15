package com.bio_library.loans.application.ports.out;

import com.bio_library.loans.domain.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILoanPersistencePort {
    Loan save(Loan loan);
    Loan findById(Long id);
    long countActiveLoansByStudentId(Long studentId);
    Page<Loan> findLoansByStudentId(Long studentId, Boolean active, Pageable pageable);
}
