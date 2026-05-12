package com.bio_library.catalog.infrastructure.adapters.driven.mongodb.adapter;

import com.bio_library.catalog.domain.constants.DomainConstants;
import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;
import com.bio_library.catalog.application.ports.out.IBookPersistencePort;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.BookDocument;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.mapper.IBookDocumentMapper;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.repository.IBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookPersistenceAdapter implements IBookPersistencePort {

    private final IBookRepository bookRepository;
    private final IBookDocumentMapper mapper;

    @Override
    public PageResult<Book> findAll(int page, int size) {
        log.info(DomainConstants.LOG_FIND_ALL, page, size);
        Page<BookDocument> result = bookRepository.findAll(PageRequest.of(page, size));
        return PageResult.<Book>builder()
                .content(mapper.toDomainList(result.getContent()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    public Book findById(String id) {
        log.info(DomainConstants.LOG_FIND_BY_ID, id);
        return bookRepository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Book save(Book book) {
        log.info(DomainConstants.LOG_SAVE, book.getId());
        BookDocument saved = bookRepository.save(mapper.toDocument(book));
        log.info(DomainConstants.LOG_SAVED, saved.getId());
        return mapper.toDomain(saved);
    }
}
