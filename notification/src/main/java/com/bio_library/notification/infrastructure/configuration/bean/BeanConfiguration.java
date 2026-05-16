package com.bio_library.notification.infrastructure.configuration.bean;

import com.bio_library.notification.application.ports.in.ILoanNotificationServicePort;
import com.bio_library.notification.application.ports.out.ISmsNotificationPort;
import com.bio_library.notification.application.usecase.LoanNotificationUseCase;
import com.bio_library.notification.domain.service.LoanNotificationDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public LoanNotificationDomainService loanNotificationDomainService() {
        return new LoanNotificationDomainService();
    }

    @Bean
    public ILoanNotificationServicePort loanNotificationServicePort(
            ISmsNotificationPort smsNotificationPort,
            LoanNotificationDomainService domainService) {
        return new LoanNotificationUseCase(smsNotificationPort, domainService);
    }
}
