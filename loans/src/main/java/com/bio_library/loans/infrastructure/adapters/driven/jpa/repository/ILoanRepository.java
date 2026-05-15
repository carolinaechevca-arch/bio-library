package com.bio_library.loans.infrastructure.adapters.driven.jpa.repository;

import com.bio_library.loans.infrastructure.adapters.driven.jpa.entity.LoanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILoanRepository extends JpaRepository<LoanEntity, Long> {
    long countByStudentIdAndActiveTrue(Long studentId);
    Page<LoanEntity> findByStudentId(Long studentId, Pageable pageable);
    Page<LoanEntity> findByStudentIdAndActive(Long studentId, Boolean active, Pageable pageable);
}
