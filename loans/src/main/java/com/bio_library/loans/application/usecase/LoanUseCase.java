package com.bio_library.loans.application.usecase;

import com.bio_library.loans.application.ports.in.ILoanServicePort;
import com.bio_library.loans.application.ports.out.ICatalogFeignClientPort;
import com.bio_library.loans.application.ports.out.ILoanPersistencePort;
import com.bio_library.loans.domain.model.Loan;
import com.bio_library.loans.domain.service.LoanDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoanUseCase implements ILoanServicePort {

    private final ILoanPersistencePort loanPersistencePort;
    private final ICatalogFeignClientPort catalogFeignClientPort;
    private final LoanDomainService loanDomainService;

    @Override
    public Loan createLoan(String bookId, Long studentId, Double gpa) {
        log.info("Creating loan: studentId={} bookId={}", studentId, bookId);
        long activeLoans = loanPersistencePort.countActiveLoansByStudentId(studentId);
        loanDomainService.validateCanBorrow(gpa, activeLoans);
        catalogFeignClientPort.incrementLoanCount(bookId);
        Loan loan = loanDomainService.prepareLoan(studentId, bookId);
        return loanPersistencePort.save(loan);
    }

    @Override
    public Loan markAsUsed(Long loanId) {
        log.info("Marking loan id={} as used", loanId);
        Loan loan = loanPersistencePort.findById(loanId);
        loanDomainService.validateLoanExists(loan, loanId);
        Loan updated = loanDomainService.markAsUsed(loan);
        return loanPersistencePort.save(updated);
    }

    @Override
    public Loan returnLoan(Long loanId, Long studentId) {
        log.info("Returning loan id={} studentId={}", loanId, studentId);
        Loan loan = loanPersistencePort.findById(loanId);
        loanDomainService.validateLoanExists(loan, loanId);
        Loan returned = loanDomainService.returnLoan(loan, studentId);
        Loan saved = loanPersistencePort.save(returned);
        catalogFeignClientPort.decrementLoanCount(loan.getBookId());
        return saved;
    }
}
