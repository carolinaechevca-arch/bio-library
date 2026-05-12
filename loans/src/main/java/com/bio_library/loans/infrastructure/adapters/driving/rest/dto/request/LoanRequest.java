package com.bio_library.loans.infrastructure.adapters.driving.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoanRequest(@NotBlank(message = "Book ID is required") String bookId) {}
