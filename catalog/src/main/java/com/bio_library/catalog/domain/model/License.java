package com.bio_library.catalog.domain.model;

import com.bio_library.catalog.domain.enums.LicenseType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class License {
    Integer maxConcurrentLoans;
    Integer activeLoanCount;
}
