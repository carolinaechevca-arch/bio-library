package com.bio_library.user.infrastructure.adapters.driving.rest.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExceptionResponse(
        String message,
        String status,
        LocalDateTime timestamp,
        Integer code) {
}