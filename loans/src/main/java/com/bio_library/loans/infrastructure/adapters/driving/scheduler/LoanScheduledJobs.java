package com.bio_library.loans.infrastructure.adapters.driving.scheduler;

import com.bio_library.loans.application.ports.in.ILoanSchedulerServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanScheduledJobs {

    private final ILoanSchedulerServicePort schedulerServicePort;

    @Scheduled(fixedRate = 300000)
    public void usageWarningJob() {
        log.info("[JOB] usageWarningJob started");
        schedulerServicePort.processUsageWarnings();
        log.info("[JOB] usageWarningJob finished");
    }

    @Scheduled(fixedRate = 300000)
    public void usageRevocationJob() {
        log.info("[JOB] usageRevocationJob started");
        schedulerServicePort.processUsageRevocations();
        log.info("[JOB] usageRevocationJob finished");
    }

    @Scheduled(fixedRate = 300000)
    public void loanExpirationJob() {
        log.info("[JOB] loanExpirationJob started");
        schedulerServicePort.processExpiredLoans();
        log.info("[JOB] loanExpirationJob finished");
    }
}
