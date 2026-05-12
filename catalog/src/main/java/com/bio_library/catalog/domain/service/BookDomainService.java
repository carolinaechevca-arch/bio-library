package com.bio_library.catalog.domain.service;

import com.bio_library.catalog.domain.constants.DomainConstants;
import com.bio_library.catalog.domain.enums.LoanAction;
import com.bio_library.catalog.domain.exceptions.BookNotFoundException;
import com.bio_library.catalog.domain.exceptions.LoanLimitExceededException;
import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.License;
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

    public Book validateAndUpdateLoanCount(Book book, LoanAction action) {
        License license = book.getLicense();
        if (action == LoanAction.INCREMENT) {
            if (license.getActiveLoanCount() >= license.getMaxConcurrentLoans()) {
                log.warn("Loan limit exceeded for book id={}, max={}", book.getId(), license.getMaxConcurrentLoans());
                throw new LoanLimitExceededException(
                        String.format(DomainConstants.LOAN_LIMIT_EXCEEDED, license.getMaxConcurrentLoans()));
            }
            log.info("Incrementing loan count for book id={}", book.getId());
            return book.toBuilder()
                    .license(license.toBuilder()
                            .activeLoanCount(license.getActiveLoanCount() + 1)
                            .build())
                    .build();
        } else {
            if (license.getActiveLoanCount() <= 0) {
                log.warn("Loan count already zero for book id={}", book.getId());
                throw new LoanLimitExceededException(DomainConstants.LOAN_COUNT_ALREADY_ZERO);
            }
            log.info("Decrementing loan count for book id={}", book.getId());
            return book.toBuilder()
                    .license(license.toBuilder()
                            .activeLoanCount(license.getActiveLoanCount() - 1)
                            .build())
                    .build();
        }
    }
}
