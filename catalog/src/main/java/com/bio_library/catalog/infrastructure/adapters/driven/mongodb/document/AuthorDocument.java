package com.bio_library.catalog.infrastructure.adapters.driven.mongodb.document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDocument {
    private String name;
    private String lastName;
}
