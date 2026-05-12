package com.bio_library.catalog.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Author {
    String name;
    String lastName;
}
