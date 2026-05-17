package com.bio_library.catalog.infrastructure.adapters.driven.mongodb.repository;

import com.bio_library.catalog.domain.enums.Category;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.BookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IBookRepository extends MongoRepository<BookDocument, String> {
    Optional<BookDocument> findByIsbn(String isbn);
    Page<BookDocument> findByCategory(Category category, Pageable pageable);
    boolean existsByIsbn(String isbn);
}
