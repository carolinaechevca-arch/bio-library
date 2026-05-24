package com.bio_library.catalog.application.ports.in;

import com.bio_library.catalog.domain.enums.Category;
import com.bio_library.catalog.domain.enums.LoanAction;
import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;

import java.util.List;

public interface IBookServicePort {
    Book createBook(Book book);
    Book updateBook(String id, Book book);
    PageResult<Book> getBooks(Category category, int page, int size);
    Book getBookById(String id);
    Book getBookByIsbn(String isbn);
    List<Category> getCategories();
    void deleteBook(String id);
    Book updateLoanCount(String id, LoanAction action);
}
