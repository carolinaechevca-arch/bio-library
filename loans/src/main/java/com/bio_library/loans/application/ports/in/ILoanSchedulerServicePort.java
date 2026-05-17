package com.bio_library.loans.application.ports.in;

public interface ILoanSchedulerServicePort {
    void processUsageWarnings();
    void processUsageRevocations();
    void processExpiredLoans();
}
