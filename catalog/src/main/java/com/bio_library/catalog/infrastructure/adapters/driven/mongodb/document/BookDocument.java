package com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document;

import com.bio_library.catalog.domain.enums.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Getter
@Setter
public class BookDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String isbn;

    private String title;
    private String author;
    private Category category;
    private String description;
    private String pdfUrl;
    private String imagenUrl;
    private Integer totalLicenses;
    private Integer availableLicenses;
}
