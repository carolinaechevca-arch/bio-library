package com.bio_library.loans.application.ports.out;

import com.bio_library.loans.domain.model.Loan;

public interface ILoanPersistencePort {
    Loan save(Loan loan);
    Loan findById(Long id);
}
