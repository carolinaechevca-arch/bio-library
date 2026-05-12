package com.bio_library.user.domain.model;

import lombok.Builder;

@Builder
public record AuthResponseModel(
        String accessToken,
        String tokenType,
        Long accessExpirationTime
) {
    public static AuthResponseModel of(String accessToken, Long accessExpirationTime) {
        return AuthResponseModel.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .accessExpirationTime(accessExpirationTime)
                .build();
    }
}
