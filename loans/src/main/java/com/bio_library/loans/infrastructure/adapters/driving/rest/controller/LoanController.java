package com.bio_library.loans.infrastructure.adapters.driving.rest.controller;

import com.bio_library.loans.application.ports.in.ILoanServicePort;
import com.bio_library.loans.domain.model.Loan;
import com.bio_library.loans.infrastructure.adapters.driven.security.model.LoanUserPrincipal;
import com.bio_library.loans.infrastructure.adapters.driving.rest.dto.request.LoanRequest;
import com.bio_library.loans.infrastructure.adapters.driving.rest.dto.response.LoanResponse;
import com.bio_library.loans.infrastructure.adapters.driving.rest.mapper.ILoanRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
@Tag(name = "Loans", description = "Book loan management")
@SecurityRequirement(name = "bearerAuth")
public class LoanController {

    private final ILoanServicePort servicePort;
    private final ILoanRestMapper restMapper;

    @Operation(summary = "Request a book loan (student only)")
    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(
            @RequestBody @Valid LoanRequest request,
            Authentication authentication) {
        LoanUserPrincipal principal = (LoanUserPrincipal) authentication.getPrincipal();
        log.info("[REST] POST loan studentId={} bookId={}", principal.id(), request.bookId());
        Loan loan = servicePort.createLoan(request.bookId(), principal.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toResponse(loan));
    }

    @Operation(summary = "Mark loan book as used")
    @PatchMapping("/{id}/mark-used")
    public ResponseEntity<LoanResponse> markAsUsed(@PathVariable Long id) {
        log.info("[REST] PATCH mark-used loanId={}", id);
        return ResponseEntity.ok(restMapper.toResponse(servicePort.markAsUsed(id)));
    }
}
