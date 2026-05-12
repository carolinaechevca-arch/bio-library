package com.bio_library.user.infrastructure.adapters.driving.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static com.bio_library.user.domain.constants.AuthConstants.*;

@Builder
public record AuthRequestDto(
        @NotBlank(message = EMAIL_REQUIRED_EXCEPTION_MESSAGE)
        @Email(message = EMAIL_INVALID_EXCEPTION_MESSAGE)
        @Size(max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_EXCEPTION_MESSAGE)
        String email,

        @NotBlank(message = PASSWORD_REQUIRED_EXCEPTION_MESSAGE)
        @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_LENGTH_EXCEPTION_MESSAGE)
        String password

) {
}