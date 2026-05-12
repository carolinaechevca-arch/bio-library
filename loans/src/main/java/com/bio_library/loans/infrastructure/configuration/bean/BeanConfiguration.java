package com.bio_library.loans.infrastructure.configuration.bean;

import com.bio_library.loans.application.ports.in.ILoanServicePort;
import com.bio_library.loans.application.ports.out.ICatalogFeignClientPort;
import com.bio_library.loans.application.ports.out.ILoanPersistencePort;
import com.bio_library.loans.application.usecase.LoanUseCase;
import com.bio_library.loans.domain.service.LoanDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public LoanDomainService loanDomainService() {
        return new LoanDomainService();
    }

    @Bean
    public ILoanServicePort loanServicePort(
            ILoanPersistencePort loanPersistencePort,
            ICatalogFeignClientPort catalogFeignClientPort,
            LoanDomainService loanDomainService) {
        return new LoanUseCase(loanPersistencePort, catalogFeignClientPort, loanDomainService);
    }
}
