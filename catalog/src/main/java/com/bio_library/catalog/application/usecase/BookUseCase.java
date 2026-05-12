package com.bio_library.catalog.application.usecase;

import com.bio_library.catalog.domain.enums.LoanAction;
import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;
import com.bio_library.catalog.application.ports.in.IBookServicePort;
import com.bio_library.catalog.application.ports.out.IBookPersistencePort;
import com.bio_library.catalog.domain.service.BookDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BookUseCase implements IBookServicePort {

    private final IBookPersistencePort bookPersistencePort;
    private final BookDomainService bookDomainService;

    @Override
    public PageResult<Book> getBooks(int page, int size) {
        log.info("Fetching books page={} size={}", page, size);
        return bookPersistencePort.findAll(page, size);
    }

    @Override
    public Book getBookById(String id) {
        log.info("Fetching book id={}", id);
        Book book = bookPersistencePort.findById(id);
        return bookDomainService.validateBookExists(book, id);
    }

    @Override
    public Book updateLoanCount(String id, LoanAction action) {
        log.info("Updating loan count for book id={} action={}", id, action);
        Book book = bookPersistencePort.findById(id);
        bookDomainService.validateBookExists(book, id);
        Book updated = bookDomainService.validateAndUpdateLoanCount(book, action);
        return bookPersistencePort.save(updated);
    }
}
