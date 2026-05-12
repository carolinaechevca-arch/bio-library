package com.bio_library.user.application.usecase;

import com.bio_library.user.application.ports.in.IAuthServicePort;
import com.bio_library.user.application.ports.out.IJwtPersistencePort;
import com.bio_library.user.application.ports.out.IPasswordEncoderPersistencePort;
import com.bio_library.user.application.ports.out.IStudentPersistencePort;
import com.bio_library.user.application.ports.out.IUserPersistencePort;
import com.bio_library.user.domain.enums.Role;
import com.bio_library.user.domain.exceptions.InvalidCredentialsException;
import com.bio_library.user.domain.model.AuthResponseModel;
import com.bio_library.user.domain.model.AuthenticationModel;
import com.bio_library.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.bio_library.user.domain.constants.AuthConstants.INVALID_CREDENTIALS_EXCEPTION_MESSAGE;


@Slf4j
@RequiredArgsConstructor
public class AuthUseCase implements IAuthServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoder;
    private final IJwtPersistencePort jwtPort;
    private final IStudentPersistencePort studentPersistencePort;

    @Override
    public AuthResponseModel authenticate(AuthenticationModel auth) {
        log.info("[USE-CASE] Authenticating user: {}", auth.email());

        User user = resolveUser(auth.email());
        validatePassword(auth.password(), user);

        log.info("[USE-CASE] Credentials validated successfully for userId={}, role={}", user.getId(), user.getRole());

        return AuthResponseModel.of(
                jwtPort.generateAccessToken(user.getEmail(), user.getId(), user.getRole().name(), resolveGpa(user)),
                jwtPort.getAccessExpirationTime()
        );
    }

    private User resolveUser(String email) {
        return Optional.ofNullable(userPersistencePort.findByEmail(email))
                .orElseThrow(() -> {
                    log.warn("[USE-CASE] User not found: {}", email);
                    return new InvalidCredentialsException(INVALID_CREDENTIALS_EXCEPTION_MESSAGE);
                });
    }

    private void validatePassword(String rawPassword, User user) {
        Optional.of(rawPassword)
                .filter(pwd -> passwordEncoder.matches(pwd, user.getPassword()))
                .orElseThrow(() -> {
                    log.warn("[USE-CASE] Invalid password for user: {}", user.getEmail());
                    return new InvalidCredentialsException(INVALID_CREDENTIALS_EXCEPTION_MESSAGE);
                });
    }

    private Double resolveGpa(User user) {
        return Optional.of(user.getRole())
                .filter(Role.STUDENT::equals)
                .map(__ -> {
                    Double gpa = studentPersistencePort.findGpaByUserId(user.getId());
                    log.info("[USE-CASE] GPA retrieved for studentId={}: {}", user.getId(), gpa);
                    return gpa;
                })
                .orElse(null);
    }
}
