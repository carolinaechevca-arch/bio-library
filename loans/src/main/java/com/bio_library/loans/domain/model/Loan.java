package com.bio_library.loans.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Loan {
    Long id;
    Long studentId;
    String bookId;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Boolean hasUsed;
    Boolean active;

    public Loan withUsed() {
        return toBuilder().hasUsed(true).build();
    }

    public Loan withReturned() {
        return toBuilder().active(false).endDate(LocalDateTime.now()).build();
    }
}
