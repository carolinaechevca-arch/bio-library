package com.bio_library.user.infrastructure.configuration.bean;

import com.bio_library.user.application.ports.in.IAuthServicePort;
import com.bio_library.user.application.ports.in.ISanctionSchedulerServicePort;
import com.bio_library.user.application.ports.in.IStudentServicePort;
import com.bio_library.user.application.ports.in.IUserProfileServicePort;
import com.bio_library.user.application.ports.out.IJwtPersistencePort;
import com.bio_library.user.application.ports.out.IPasswordEncoderPersistencePort;
import com.bio_library.user.application.ports.out.IStudentPersistencePort;
import com.bio_library.user.application.ports.out.IUniversityFeignClientPort;
import com.bio_library.user.application.ports.out.IUserPersistencePort;
import com.bio_library.user.application.usecase.AuthUseCase;
import com.bio_library.user.application.usecase.SanctionSchedulerUseCase;
import com.bio_library.user.application.usecase.StudentUseCase;
import com.bio_library.user.application.usecase.UserProfileUseCase;
import com.bio_library.user.domain.service.StudentDomainService;
import com.bio_library.user.domain.validation.StudentValidationStrategy;
import com.bio_library.user.infrastructure.adapters.driven.security.adapter.PasswordEncoderAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    @Bean
    public StudentDomainService technicianDomainService() {
        return new StudentDomainService();
    }

    @Bean
    public IStudentServicePort technicianServicePort(
            IStudentPersistencePort studentPersistencePort,
            IPasswordEncoderPersistencePort passwordEncoderPort,
            IUniversityFeignClientPort universityServicePort,
            IUserPersistencePort userPersistencePort,
            List<StudentValidationStrategy> strategies
    ) {
        return new StudentUseCase(
                studentPersistencePort,
                passwordEncoderPort,
                technicianDomainService(),
                universityServicePort,
                userPersistencePort,
                strategies
        );
    }


    @Bean
    public ISanctionSchedulerServicePort sanctionSchedulerServicePort(IStudentPersistencePort studentPersistencePort) {
        return new SanctionSchedulerUseCase(studentPersistencePort);
    }

    @Bean
    public IPasswordEncoderPersistencePort passwordEncoderPersistencePort(PasswordEncoder passwordEncoder) {
        return new PasswordEncoderAdapter(passwordEncoder);
    }

    @Bean
    public IAuthServicePort authServicePort(
            IUserPersistencePort userPersistencePort,
            IPasswordEncoderPersistencePort passwordEncoderPersistencePort,
            IJwtPersistencePort jwtPersistencePort,
            IStudentPersistencePort studentPersistencePort) {
        return new AuthUseCase(
                userPersistencePort,
                passwordEncoderPersistencePort,
                jwtPersistencePort,
                studentPersistencePort);
    }

    @Bean
    public IUserProfileServicePort userProfileServicePort(
            IUserPersistencePort userPersistencePort,
            IStudentPersistencePort studentPersistencePort) {
        return new UserProfileUseCase(userPersistencePort, studentPersistencePort);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(IUserPersistencePort userPersistencePort) {
        return username -> Optional.ofNullable(userPersistencePort.findByEmail(username))
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .authorities("ROLE_" + user.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(IUserPersistencePort userPersistencePort) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService(userPersistencePort));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}