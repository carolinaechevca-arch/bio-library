package com.bio_library.loans.application.usecase;

import com.bio_library.loans.application.ports.in.ILoanServicePort;
import com.bio_library.loans.application.ports.out.ICatalogFeignClientPort;
import com.bio_library.loans.application.ports.out.ILoanPersistencePort;
import com.bio_library.loans.domain.factory.LoanFactory;
import com.bio_library.loans.domain.model.Loan;
import com.bio_library.loans.domain.service.LoanDomainService;
import com.bio_library.loans.domain.validation.ILoanCreationRule;
import com.bio_library.loans.domain.validation.ILoanReturnRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LoanUseCase implements ILoanServicePort {

    private final ILoanPersistencePort loanPersistencePort;
    private final ICatalogFeignClientPort catalogFeignClientPort;
    private final LoanDomainService loanDomainService;
    private final List<ILoanCreationRule> creationRules;
    private final List<ILoanReturnRule> returnRules;

    @Override
    public Loan createLoan(String bookId, Long studentId, Double gpa) {
        log.info("Creating loan: studentId={} bookId={}", studentId, bookId);
        long activeLoans = loanPersistencePort.countActiveLoansByStudentId(studentId);
        creationRules.forEach(rule -> rule.validate(gpa, activeLoans));
        catalogFeignClientPort.incrementLoanCount(bookId);
        return loanPersistencePort.save(LoanFactory.newLoan(studentId, bookId));
    }

    @Override
    public Loan markAsUsed(Long loanId) {
        log.info("Marking loan id={} as used", loanId);
        Loan loan = loanDomainService.validateLoanExists(loanPersistencePort.findById(loanId), loanId);
        return loanPersistencePort.save(loan.withUsed());
    }

    @Override
    public Loan returnLoan(Long loanId, Long studentId) {
        log.info("Returning loan id={} studentId={}", loanId, studentId);
        Loan loan = loanDomainService.validateLoanExists(loanPersistencePort.findById(loanId), loanId);
        returnRules.forEach(rule -> rule.validate(loan, studentId));
        Loan saved = loanPersistencePort.save(loan.withReturned());
        catalogFeignClientPort.decrementLoanCount(loan.getBookId());
        return saved;
    }

    @Override
    public Page<Loan> getLoansByStudentId(Long studentId, Boolean active, Pageable pageable) {
        log.info("Listing loans studentId={} active={} page={}", studentId, active, pageable.getPageNumber());
        return loanPersistencePort.findLoansByStudentId(studentId, active, pageable);
    }
}
