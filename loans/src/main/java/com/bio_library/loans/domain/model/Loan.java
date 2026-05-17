package com.bio_library.loans.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class Loan {
    Long id;
    Long studentId;
    String bookId;
    LocalDate startDate;
    LocalDate endDate;
    Boolean hasUsed;
    Boolean active;

    public Loan withUsed() {
        return toBuilder().hasUsed(true).build();
    }

    public Loan withReturned() {
        return toBuilder().active(false).endDate(LocalDate.now()).build();
    }
}
