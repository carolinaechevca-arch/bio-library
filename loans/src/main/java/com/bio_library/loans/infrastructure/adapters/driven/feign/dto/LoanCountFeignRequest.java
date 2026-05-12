package com.bio_library.loans.infrastructure.adapters.driven.feign.dto;

public record LoanCountFeignRequest(String action) {
    public static LoanCountFeignRequest increment() {
        return new LoanCountFeignRequest("INCREMENT");
    }
}
