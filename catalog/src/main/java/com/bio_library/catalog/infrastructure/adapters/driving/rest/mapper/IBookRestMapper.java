package com.bio_library.catalog.infrastructure.adapters.driving.rest.mapper;

import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.request.BookRequest;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response.BookResponse;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response.PageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IBookRestMapper {

    BookResponse toResponse(Book book);

    List<BookResponse> toResponseList(List<Book> books);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableLicenses", ignore = true)
    Book toBook(BookRequest request);

    default PageResponse<BookResponse> toPageResponse(PageResult<Book> pageResult) {
        return PageResponse.<BookResponse>builder()
                .content(pageResult.getContent().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList()))
                .page(pageResult.getPage())
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }
}
