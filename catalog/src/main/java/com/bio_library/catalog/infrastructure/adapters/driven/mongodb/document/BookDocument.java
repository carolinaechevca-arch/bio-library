package com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document;

import com.bio_library.catalog.domain.enums.Category;
import com.bio_library.catalog.domain.enums.Language;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "books")
@Getter
@Setter
public class BookDocument {

    @Id
    private String id;
    private String isbn;
    private String title;
    private AuthorDocument author;
    private Category category;
    private String publisher;
    private Integer year;
    private Integer edition;
    private Language language;
    private String synopsis;
    private Integer pages;
    private String pdfUrl;
    private String coverImageUrl;
    private LicenseDocument license;
    private Boolean active;
}
