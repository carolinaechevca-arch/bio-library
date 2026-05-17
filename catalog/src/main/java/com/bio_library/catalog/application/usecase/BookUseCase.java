package com.bio_library.catalog.application.usecase;

import com.bio_library.catalog.domain.constants.DomainConstants;
import com.bio_library.catalog.domain.enums.Category;
import com.bio_library.catalog.domain.enums.LoanAction;
import com.bio_library.catalog.domain.exceptions.BookAlreadyExistsException;
import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;
import com.bio_library.catalog.application.ports.in.IBookServicePort;
import com.bio_library.catalog.application.ports.out.IBookPersistencePort;
import com.bio_library.catalog.domain.service.BookDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BookUseCase implements IBookServicePort {

    private final IBookPersistencePort bookPersistencePort;
    private final BookDomainService bookDomainService;

    @Override
    public Book createBook(Book book) {
        log.info("[CREATE] Creating book isbn={}", book.getIsbn());
        if (bookPersistencePort.existsByIsbn(book.getIsbn())) {
            throw new BookAlreadyExistsException(
                    String.format(DomainConstants.BOOK_ALREADY_EXISTS, book.getIsbn()));
        }
        Book toSave = book.toBuilder()
                .availableLicenses(book.getTotalLicenses())
                .build();
        Book saved = bookPersistencePort.save(toSave);
        log.info("[CREATE-SUCCESS] Book saved with id={}", saved.getId());
        return saved;
    }

    @Override
    public PageResult<Book> getBooks(Category category, int page, int size) {
        log.info("[LIST] Fetching books category={} page={} size={}", category, page, size);
        return bookPersistencePort.findAll(category, page, size);
    }

    @Override
    public Book getBookById(String id) {
        log.info("[GET] Fetching book id={}", id);
        return bookDomainService.validateBookExists(bookPersistencePort.findById(id), id);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        log.info("[GET] Fetching book isbn={}", isbn);
        return bookDomainService.validateBookExistsByIsbn(bookPersistencePort.findByIsbn(isbn), isbn);
    }

    @Override
    public List<Category> getCategories() {
        return Arrays.asList(Category.values());
    }

    @Override
    public void deleteBook(String id) {
        log.info("[DELETE] Deleting book id={}", id);
        bookDomainService.validateBookExists(bookPersistencePort.findById(id), id);
        bookPersistencePort.deleteById(id);
        log.info("[DELETE-SUCCESS] Book deleted id={}", id);
    }

    @Override
    public Book updateLoanCount(String id, LoanAction action) {
        log.info("[LOAN-COUNT] Updating book id={} action={}", id, action);
        Book book = bookDomainService.validateBookExists(bookPersistencePort.findById(id), id);
        Book updated = bookDomainService.validateAndUpdateLoanCount(book, action);
        return bookPersistencePort.save(updated);
    }
}
