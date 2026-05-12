package com.bio_library.loans.infrastructure.adapters.driven.feign;

import com.bio_library.loans.infrastructure.adapters.driven.feign.dto.LoanCountFeignRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "catalog", url = "${catalog.url}")
public interface ICatalogFeignClient {

    @PatchMapping("/api/v1/books/{id}/loan-count")
    void updateLoanCount(@PathVariable String id, @RequestBody LoanCountFeignRequest request);
}
