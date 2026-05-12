package com.bio_library.catalog.infrastructure.adapters.driven.mongodb.repository;

import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.BookDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IBookRepository extends MongoRepository<BookDocument, String> {
}
