package com.bio_library.loans.application.ports.out;

public interface ICatalogFeignClientPort {
    void incrementLoanCount(String bookId);
}
