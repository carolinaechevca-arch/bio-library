package com.bio_library.loans.infrastructure.adapters.driven.jpa.repository;

import com.bio_library.loans.infrastructure.adapters.driven.jpa.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILoanRepository extends JpaRepository<LoanEntity, Long> {
}
