package com.bio_library.loans.infrastructure.adapters.driving.rest.dto.response;

import java.time.LocalDate;

public record LoanResponse(
        Long id,
        Long studentId,
        String bookId,
        LocalDate startDate,
        LocalDate endDate,
        Boolean hasUsed,
        Boolean active) {}
