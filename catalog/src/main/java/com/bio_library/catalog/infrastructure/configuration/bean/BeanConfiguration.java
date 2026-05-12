package com.bio_library.catalog.infrastructure.configuration.bean;

import com.bio_library.catalog.application.usecase.BookUseCase;
import com.bio_library.catalog.application.ports.in.IBookServicePort;
import com.bio_library.catalog.application.ports.out.IBookPersistencePort;
import com.bio_library.catalog.domain.service.BookDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public BookDomainService bookDomainService() {
        return new BookDomainService();
    }

    @Bean
    public IBookServicePort bookServicePort(
            IBookPersistencePort bookPersistencePort,
            BookDomainService bookDomainService) {
        return new BookUseCase(bookPersistencePort, bookDomainService);
    }
}
