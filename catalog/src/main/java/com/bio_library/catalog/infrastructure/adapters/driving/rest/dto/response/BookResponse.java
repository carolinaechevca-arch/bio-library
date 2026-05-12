package com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response;

import com.bio_library.catalog.domain.enums.Category;
import com.bio_library.catalog.domain.enums.Language;

import java.time.LocalDateTime;

public record BookResponse(
        String id,
        String isbn,
        String title,
        AuthorResponse author,
        Category category,
        String publisher,
        Integer year,
        Integer edition,
        Language language,
        String synopsis,
        Integer pages,
        String pdfUrl,
        String coverImageUrl,
        LicenseResponse license,
        Boolean active) {}
