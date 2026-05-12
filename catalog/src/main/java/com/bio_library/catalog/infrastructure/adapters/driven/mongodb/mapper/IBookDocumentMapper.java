package com.bio_library.catalog.infrastructure.adapters.driven.mongodb.mapper;

import com.bio_library.catalog.domain.model.Author;
import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.License;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.AuthorDocument;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.BookDocument;
import com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document.LicenseDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IBookDocumentMapper {
    Book toDomain(BookDocument document);
    BookDocument toDocument(Book book);
    List<Book> toDomainList(List<BookDocument> documents);
    Author toDomain(AuthorDocument authorDocument);
    AuthorDocument toDocument(Author author);
    License toDomain(LicenseDocument licenseDocument);
    LicenseDocument toDocument(License license);
}
