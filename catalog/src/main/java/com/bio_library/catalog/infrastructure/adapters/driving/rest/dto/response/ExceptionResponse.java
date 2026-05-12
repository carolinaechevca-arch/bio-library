package com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response;

import java.time.LocalDateTime;

public record ExceptionResponse(String message, String status, LocalDateTime timestamp, int statusCode) {}
