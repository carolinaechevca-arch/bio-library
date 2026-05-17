package com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response;

import com.bio_library.catalog.domain.enums.Category;

public record BookResponse(
        String id,
        String isbn,
        String title,
        String author,
        Category category,
        String description,
        String pdfUrl,
        String imagenUrl,
        Integer totalLicenses,
        Integer availableLicenses
) {}
