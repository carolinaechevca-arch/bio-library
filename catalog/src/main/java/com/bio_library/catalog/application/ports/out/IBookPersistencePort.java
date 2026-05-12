package com.bio_library.catalog.application.ports.out;

import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;

public interface IBookPersistencePort {
    PageResult<Book> findAll(int page, int size);
    Book findById(String id);
    Book save(Book book);
}
