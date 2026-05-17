package com.bio_library.catalog.domain.service;

import com.bio_library.catalog.domain.constants.DomainConstants;
import com.bio_library.catalog.domain.enums.LoanAction;
import com.bio_library.catalog.domain.exceptions.BookNotFoundException;
import com.bio_library.catalog.domain.exceptions.LoanLimitExceededException;
import com.bio_library.catalog.domain.model.Book;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class BookDomainService {

    public Book validateBookExists(Book book, String id) {
        return Optional.ofNullable(book).orElseThrow(() -> {
            log.warn("Book with id {} was not found", id);
            return new BookNotFoundException(String.format(DomainConstants.BOOK_NOT_FOUND, id));
        });
    }

    public Book validateBookExistsByIsbn(Book book, String isbn) {
        return Optional.ofNullable(book).orElseThrow(() -> {
            log.warn("Book with isbn {} was not found", isbn);
            return new BookNotFoundException(String.format(DomainConstants.BOOK_NOT_FOUND_BY_ISBN, isbn));
        });
    }

    public Book validateAndUpdateLoanCount(Book book, LoanAction action) {
        if (action == LoanAction.INCREMENT) {
            if (book.getAvailableLicenses() <= 0) {
                log.warn("No available licenses for book id={}, total={}", book.getId(), book.getTotalLicenses());
                throw new LoanLimitExceededException(
                        String.format(DomainConstants.LOAN_LIMIT_EXCEEDED, book.getTotalLicenses()));
            }
            log.info("Decrementing available licenses for book id={}", book.getId());
            return book.toBuilder()
                    .availableLicenses(book.getAvailableLicenses() - 1)
                    .build();
        } else {
            if (book.getAvailableLicenses() >= book.getTotalLicenses()) {
                log.warn("All licenses already available for book id={}", book.getId());
                throw new LoanLimitExceededException(DomainConstants.LOAN_COUNT_ALREADY_FULL);
            }
            log.info("Incrementing available licenses for book id={}", book.getId());
            return book.toBuilder()
                    .availableLicenses(book.getAvailableLicenses() + 1)
                    .build();
        }
    }
}
