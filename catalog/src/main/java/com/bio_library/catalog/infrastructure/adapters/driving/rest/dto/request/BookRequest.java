package com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.request;

import com.bio_library.catalog.domain.enums.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BookRequest(
        @NotBlank(message = "isbn is required")
        String isbn,

        @NotBlank(message = "title is required")
        String title,

        @NotBlank(message = "author is required")
        String author,

        @NotNull(message = "category is required")
        Category category,

        @NotBlank(message = "description is required")
        String description,

        @NotBlank(message = "pdfUrl is required")
        String pdfUrl,

        String imagenUrl,

        @NotNull(message = "totalLicenses is required")
        @Min(value = 1, message = "totalLicenses must be at least 1")
        Integer totalLicenses
) {}
