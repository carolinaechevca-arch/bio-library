package com.bio_library.user.domain.model;

import lombok.Builder;

@Builder
public record AuthenticationModel(
        String email,
        String password
) {
}