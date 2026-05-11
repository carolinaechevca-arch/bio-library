package com.bio_library.user.domain.model;

import lombok.Builder;

@Builder
public record AuthResponseModel(
        String accessToken,
        String tokenType,
        Long accessExpirationTime
) {
}