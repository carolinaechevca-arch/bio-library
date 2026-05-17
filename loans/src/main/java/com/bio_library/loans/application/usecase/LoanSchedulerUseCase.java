package com.bio_library.loans.application.usecase;

import com.bio_library.loans.application.ports.in.ILoanSchedulerServicePort;
import com.bio_library.loans.application.ports.out.ICatalogFeignClientPort;
import com.bio_library.loans.application.ports.out.ILoanPersistencePort;
import com.bio_library.loans.application.ports.out.INotificationPort;
import com.bio_library.loans.application.ports.out.IUserFeignClientPort;
import com.bio_library.loans.application.ports.out.StudentContactInfo;
import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.model.Loan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LoanSchedulerUseCase implements ILoanSchedulerServicePort {

    private final ILoanPersistencePort loanPersistencePort;
    private final ICatalogFeignClientPort catalogFeignClientPort;
    private final INotificationPort notificationPort;
    private final IUserFeignClientPort userFeignClientPort;

    @Override
    public void processUsageWarnings() {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(DomainConstants.USAGE_REVOCATION_DAYS);
        LocalDate to = today.minusDays(DomainConstants.USAGE_WARNING_DAYS);
        List<Loan> loans = loanPersistencePort.findActiveUnusedLoansBetween(from, to);
        log.info("[SCHEDULER] Usage warning: {} loans to notify", loans.size());
        loans.forEach(loan -> {
            StudentContactInfo contact = userFeignClientPort.getStudentContact(loan.getStudentId());
            if (contact != null && contact.email() != null && !contact.email().isBlank()) {
                notificationPort.notifyLoanUsageWarning(loan.getStudentId(), contact.email(), contact.phone(), loan.getBookId());
            }
        });
    }

    @Override
    public void processUsageRevocations() {
        LocalDate cutoff = LocalDate.now().minusDays(DomainConstants.USAGE_REVOCATION_DAYS);
        List<Loan> loans = loanPersistencePort.findActiveUnusedLoansOlderThan(cutoff);
        log.info("[SCHEDULER] Usage revocation: {} loans to revoke", loans.size());
        loans.forEach(loan -> {
            loanPersistencePort.save(loan.withReturned());
            catalogFeignClientPort.decrementLoanCount(loan.getBookId());
            StudentContactInfo contact = userFeignClientPort.getStudentContact(loan.getStudentId());
            if (contact != null && contact.email() != null && !contact.email().isBlank()) {
                notificationPort.notifyLoanUsageRevoked(loan.getStudentId(), contact.email(), contact.phone(), loan.getBookId());
            }
        });
    }

    @Override
    public void processExpiredLoans() {
        LocalDate cutoff = LocalDate.now().minusDays(DomainConstants.LOAN_MAX_DURATION_DAYS);
        List<Loan> loans = loanPersistencePort.findActiveLoansOlderThan(cutoff);
        log.info("[SCHEDULER] Expiration: {} loans to expire", loans.size());
        loans.forEach(loan -> {
            loanPersistencePort.save(loan.withReturned());
            catalogFeignClientPort.decrementLoanCount(loan.getBookId());
            StudentContactInfo contact = userFeignClientPort.getStudentContact(loan.getStudentId());
            if (contact != null && contact.email() != null && !contact.email().isBlank()) {
                notificationPort.notifyLoanExpired(loan.getStudentId(), contact.email(), contact.phone(), loan.getBookId());
            }
        });
    }
}
