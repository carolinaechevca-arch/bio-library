package com.bio_library.catalog.infrastructure.adapters.driven.mongodb.mapper;

import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.BookDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IBookDocumentMapper {
    Book toDomain(BookDocument document);
    BookDocument toDocument(Book book);
    List<Book> toDomainList(List<BookDocument> documents);
}
