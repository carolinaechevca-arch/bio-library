package com.bio_library.loans.infrastructure.configuration.bean;

import com.bio_library.loans.application.ports.in.ILoanServicePort;
import com.bio_library.loans.application.ports.out.ICatalogFeignClientPort;
import com.bio_library.loans.application.ports.out.ILoanPersistencePort;
import com.bio_library.loans.application.usecase.LoanUseCase;
import com.bio_library.loans.domain.service.LoanDomainService;
import com.bio_library.loans.domain.validation.ILoanCreationRule;
import com.bio_library.loans.domain.validation.ILoanReturnRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
            LoanDomainService loanDomainService,
            List<ILoanCreationRule> creationRules,
            List<ILoanReturnRule> returnRules) {
        return new LoanUseCase(loanPersistencePort, catalogFeignClientPort,
                loanDomainService, creationRules, returnRules);
    }
}
