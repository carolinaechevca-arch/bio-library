package com.bio_library.loans.infrastructure.configuration.exceptionhandler;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ExceptionResponse {
    String message;
    String status;
    LocalDateTime timestamp;
}
