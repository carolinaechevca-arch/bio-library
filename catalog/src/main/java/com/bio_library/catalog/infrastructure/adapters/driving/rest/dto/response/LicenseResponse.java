package com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response;

import com.bio_library.catalog.domain.enums.LicenseType;

import java.time.LocalDateTime;

public record LicenseResponse(
        Integer maxConcurrentLoans,
        Integer activeLoanCount) {}
