package com.bio_library.loans.infrastructure.adapters.driven.jpa.adapter;

import com.bio_library.loans.application.ports.out.ILoanPersistencePort;
import com.bio_library.loans.domain.model.Loan;
import com.bio_library.loans.infrastructure.adapters.driven.jpa.entity.LoanEntity;
import com.bio_library.loans.infrastructure.adapters.driven.jpa.mapper.ILoanEntityMapper;
import com.bio_library.loans.infrastructure.adapters.driven.jpa.repository.ILoanRepository;
import com.bio_library.loans.infrastructure.adapters.driven.util.PersistenceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanPersistenceAdapter implements ILoanPersistencePort {

    private final ILoanRepository loanRepository;
    private final ILoanEntityMapper mapper;

    @Override
    public Loan save(Loan loan) {
        log.info(PersistenceConstants.LOAN_SAVE, loan.getStudentId(), loan.getBookId());
        LoanEntity saved = loanRepository.save(mapper.toEntity(loan));
        log.info(PersistenceConstants.LOAN_SAVED, saved.getId());
        return mapper.toDomain(saved);
    }

    @Override
    public Loan findById(Long id) {
        log.info(PersistenceConstants.LOAN_FIND_BY_ID, id);
        return loanRepository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public long countActiveLoansByStudentId(Long studentId) {
        log.info("[LOAN-DB] Counting active loans for studentId={}", studentId);
        return loanRepository.countByStudentIdAndActiveTrue(studentId);
    }

    @Override
    public Page<Loan> findLoansByStudentId(Long studentId, Boolean active, Pageable pageable) {
        log.info(PersistenceConstants.LOAN_FIND_BY_STUDENT, studentId, active);
        Page<LoanEntity> page = Optional.ofNullable(active)
                .map(a -> loanRepository.findByStudentIdAndActive(studentId, a, pageable))
                .orElseGet(() -> loanRepository.findByStudentId(studentId, pageable));
        return page.map(mapper::toDomain);
    }
}
