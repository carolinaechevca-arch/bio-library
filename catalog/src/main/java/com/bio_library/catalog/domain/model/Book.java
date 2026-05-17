package com.bio_library.catalog.domain.model;

import com.bio_library.catalog.domain.enums.Category;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Book {
    String id;
    String isbn;
    String title;
    String author;
    Category category;
    String description;
    String pdfUrl;
    String imagenUrl;
    Integer totalLicenses;
    Integer availableLicenses;
}
