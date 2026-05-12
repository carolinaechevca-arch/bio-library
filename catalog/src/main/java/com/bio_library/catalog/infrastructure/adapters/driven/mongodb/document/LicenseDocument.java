package com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document;

import com.bio_library.catalog.domain.enums.LicenseType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LicenseDocument {
    private Integer maxConcurrentLoans;
    private Integer activeLoanCount;
}
