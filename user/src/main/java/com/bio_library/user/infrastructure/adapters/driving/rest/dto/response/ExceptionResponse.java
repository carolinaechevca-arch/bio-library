package com.bio_library.user.infrastructure.adapters.driving.rest.dto.response;



import java.time.LocalDateTime;

public record ExceptionResponse(
        String message,
        String status,
        LocalDateTime timestamp,
        Integer code) {
}