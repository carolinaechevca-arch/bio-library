package com.bio_library.loans.infrastructure.adapters.driven.feign.adapter;

import com.bio_library.loans.application.ports.out.ICatalogFeignClientPort;
import com.bio_library.loans.domain.constants.DomainConstants;
import com.bio_library.loans.domain.exceptions.BookNotAvailableException;
import com.bio_library.loans.domain.exceptions.BookNotFoundException;
import com.bio_library.loans.infrastructure.adapters.driven.feign.ICatalogFeignClient;
import com.bio_library.loans.infrastructure.adapters.driven.feign.dto.LoanCountFeignRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogFeignClientAdapter implements ICatalogFeignClientPort {

    private final ICatalogFeignClient feignClient;

    @Override
    public void incrementLoanCount(String bookId) {
        log.info("[FEIGN] Incrementing loan count for book id={}", bookId);
        try {
            feignClient.updateLoanCount(bookId, LoanCountFeignRequest.increment());
            log.info("[FEIGN] Loan count incremented successfully for book id={}", bookId);
        } catch (FeignException.NotFound e) {
            log.warn("[FEIGN] Book not found in catalog: id={}", bookId);
            throw new BookNotFoundException(String.format(DomainConstants.BOOK_NOT_FOUND, bookId));
        } catch (FeignException.UnprocessableEntity e) {
            log.warn("[FEIGN] Book has no available copies: id={}", bookId);
            throw new BookNotAvailableException(String.format(DomainConstants.BOOK_NOT_AVAILABLE, bookId));
        }
    }
}
