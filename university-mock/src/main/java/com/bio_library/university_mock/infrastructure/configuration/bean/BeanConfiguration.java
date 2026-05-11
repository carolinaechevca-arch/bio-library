package com.bio_library.university_mock.infrastructure.configuration.bean;

import com.bio_library.university_mock.application.usecase.UniversityStudentUseCase;
import com.bio_library.university_mock.domain.ports.in.IUniversityStudentServicePort;
import com.bio_library.university_mock.domain.ports.out.IUniversityStudentPersistencePort;
import com.bio_library.university_mock.domain.service.UniversityStudentDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public UniversityStudentDomainService universityStudentDomainService() {
        return new UniversityStudentDomainService();
    }

    @Bean
    public IUniversityStudentServicePort universityStudentServicePort(
            IUniversityStudentPersistencePort persistencePort) {
        return new UniversityStudentUseCase(persistencePort, universityStudentDomainService());
    }
}
