package com.bio_library.catalog.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PageResult<T> {
    List<T> content;
    int page;
    int size;
    long totalElements;
    int totalPages;
}
