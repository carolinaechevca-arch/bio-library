package com.bio_library.loans.infrastructure.adapters.driven.jpa.repository;

import com.bio_library.loans.infrastructure.adapters.driven.jpa.entity.LoanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ILoanRepository extends JpaRepository<LoanEntity, Long> {
    long countByStudentIdAndActiveTrue(Long studentId);
    Page<LoanEntity> findByStudentId(Long studentId, Pageable pageable);
    Page<LoanEntity> findByStudentIdAndActive(Long studentId, Boolean active, Pageable pageable);

    List<LoanEntity> findByActiveTrueAndHasUsedFalseAndStartDateBetween(LocalDate from, LocalDate to);
    List<LoanEntity> findByActiveTrueAndHasUsedFalseAndStartDateBefore(LocalDate cutoff);
    List<LoanEntity> findByActiveTrueAndStartDateBefore(LocalDate cutoff);
}
