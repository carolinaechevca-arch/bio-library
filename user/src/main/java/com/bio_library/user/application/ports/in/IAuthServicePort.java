package com.bio_library.user.application.ports.in;

import com.bio_library.user.domain.model.AuthResponseModel;
import com.bio_library.user.domain.model.AuthenticationModel;

public interface IAuthServicePort {

    AuthResponseModel authenticate(AuthenticationModel auth);
}
