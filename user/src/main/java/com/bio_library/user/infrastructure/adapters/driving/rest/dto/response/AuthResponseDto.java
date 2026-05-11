package com.bio_library.user.infrastructure.adapters.driving.rest.dto.response;

import lombok.Builder;

@Builder
public record AuthResponseDto(
        String accessToken,
        String tokenType,
        Long accessExpirationTime
) {
}