package com.bio_library.user.application.ports.in;

import com.bio_library.user.domain.model.UserProfileModel;

public interface IUserProfileServicePort {
    UserProfileModel getProfile(String email);
}
