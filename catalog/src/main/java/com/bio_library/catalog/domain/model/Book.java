package com.bio_library.catalog.domain.model;

import com.bio_library.catalog.domain.enums.Category;
import com.bio_library.catalog.domain.enums.Language;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Book {
    String id;
    String isbn;
    String title;
    Author author;
    Category category;
    String publisher;
    Integer year;
    Integer edition;
    Language language;
    String synopsis;
    Integer pages;
    String pdfUrl;
    String coverImageUrl;
    License license;
    Boolean active;
}
