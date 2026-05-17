package com.bio_library.catalog.application.ports.out;

import com.bio_library.catalog.domain.enums.Category;
import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;

public interface IBookPersistencePort {
    Book save(Book book);
    PageResult<Book> findAll(Category category, int page, int size);
    Book findById(String id);
    Book findByIsbn(String isbn);
    void deleteById(String id);
    boolean existsByIsbn(String isbn);
}
