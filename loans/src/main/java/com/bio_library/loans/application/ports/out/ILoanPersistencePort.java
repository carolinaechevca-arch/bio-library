package com.bio_library.loans.application.ports.out;

import com.bio_library.loans.domain.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ILoanPersistencePort {
    Loan save(Loan loan);
    Loan findById(Long id);
    long countActiveLoansByStudentId(Long studentId);
    Page<Loan> findLoansByStudentId(Long studentId, Boolean active, Pageable pageable);

    List<Loan> findActiveUnusedLoansBetween(LocalDate from, LocalDate to);
    List<Loan> findActiveUnusedLoansOlderThan(LocalDate cutoff);
    List<Loan> findActiveLoansOlderThan(LocalDate cutoff);
}
