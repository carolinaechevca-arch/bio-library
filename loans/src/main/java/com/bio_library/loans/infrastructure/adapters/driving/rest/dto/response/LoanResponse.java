package com.bio_library.loans.infrastructure.adapters.driving.rest.dto.response;

import java.time.LocalDateTime;

public record LoanResponse(
        Long id,
        Long studentId,
        String bookId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean hasUsed,
        Boolean active) {}
