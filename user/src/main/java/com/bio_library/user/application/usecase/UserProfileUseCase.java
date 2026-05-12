package com.bio_library.user.application.usecase;

import com.bio_library.user.application.ports.in.IUserProfileServicePort;
import com.bio_library.user.application.ports.out.IStudentPersistencePort;
import com.bio_library.user.application.ports.out.IUserPersistencePort;
import com.bio_library.user.domain.enums.Role;
import com.bio_library.user.domain.model.User;
import com.bio_library.user.domain.model.UserProfileModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserProfileUseCase implements IUserProfileServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IStudentPersistencePort studentPersistencePort;

    @Override
    public UserProfileModel getProfile(String email) {
        log.info("[PROFILE] Fetching profile for: {}", email);
        User user = userPersistencePort.findByEmail(email);

        return Optional.of(user.getRole())
                .filter(Role.STUDENT::equals)
                .flatMap(__ -> studentPersistencePort.findByUserId(user.getId()))
                .map(UserProfileModel::from)
                .orElseGet(() -> UserProfileModel.from(user));
    }
}
