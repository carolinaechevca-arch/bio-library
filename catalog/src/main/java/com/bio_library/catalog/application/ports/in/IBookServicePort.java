package com.bio_library.catalog.application.ports.in;

import com.bio_library.catalog.domain.enums.LoanAction;
import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;

public interface IBookServicePort {
    PageResult<Book> getBooks(int page, int size);
    Book getBookById(String id);
    Book updateLoanCount(String id, LoanAction action);
}
