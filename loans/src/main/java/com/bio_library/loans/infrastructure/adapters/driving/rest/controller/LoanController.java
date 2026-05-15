package com.bio_library.loans.infrastructure.adapters.driving.rest.controller;

import com.bio_library.loans.application.ports.in.ILoanServicePort;
import com.bio_library.loans.domain.model.Loan;
import com.bio_library.loans.infrastructure.adapters.driven.security.model.LoanUserPrincipal;
import com.bio_library.loans.infrastructure.adapters.driving.rest.dto.request.LoanRequest;
import com.bio_library.loans.infrastructure.adapters.driving.rest.dto.response.LoanResponse;
import com.bio_library.loans.infrastructure.adapters.driving.rest.mapper.ILoanRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        Loan loan = servicePort.createLoan(request.bookId(), principal.id(), principal.gpa());
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toResponse(loan));
    }

    @Operation(summary = "Mark loan book as used")
    @PatchMapping("/{id}/mark-used")
    public ResponseEntity<LoanResponse> markAsUsed(@PathVariable Long id) {
        log.info("[REST] PATCH mark-used loanId={}", id);
        return ResponseEntity.ok(restMapper.toResponse(servicePort.markAsUsed(id)));
    }

    @Operation(summary = "Return a borrowed book and release the license")
    @PatchMapping("/{id}/return")
    public ResponseEntity<LoanResponse> returnLoan(
            @PathVariable Long id,
            Authentication authentication) {
        LoanUserPrincipal principal = (LoanUserPrincipal) authentication.getPrincipal();
        log.info("[REST] PATCH return loanId={} studentId={}", id, principal.id());
        return ResponseEntity.ok(restMapper.toResponse(servicePort.returnLoan(id, principal.id())));
    }

    @Operation(
            summary = "List my loans",
            description = "Returns a paginated list of the authenticated student's loans, optionally filtered by status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated list of loans"),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = com.bio_library.loans.infrastructure.configuration.exceptionhandler.ExceptionResponse.class)))
    })
    @GetMapping("/my-loans")
    public ResponseEntity<Page<LoanResponse>> getMyLoans(
            @Parameter(description = "Filter by loan status: true = active, false = returned, omit = all")
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by (startDate, endDate, bookId)", example = "startDate")
            @RequestParam(defaultValue = "startDate") String sortBy,
            @Parameter(description = "Sort direction: asc or desc", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication) {

        LoanUserPrincipal principal = (LoanUserPrincipal) authentication.getPrincipal();
        log.info("[REST] GET /my-loans studentId={} active={} page={} size={} sortBy={} sortDir={}",
                principal.id(), active, page, size, sortBy, sortDir);

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(servicePort.getLoansByStudentId(principal.id(), active, pageable)
                .map(restMapper::toResponse));
    }

    @Operation(
            summary = "List loans by student (ADMIN only)",
            description = "Returns a paginated list of loans for a specific student. Requires ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated list of loans"),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = com.bio_library.loans.infrastructure.configuration.exceptionhandler.ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = com.bio_library.loans.infrastructure.configuration.exceptionhandler.ExceptionResponse.class)))
    })
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<LoanResponse>> getLoansByStudent(
            @PathVariable Long studentId,
            @Parameter(description = "Filter by loan status: true = active, false = returned, omit = all")
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by (startDate, endDate, bookId)", example = "startDate")
            @RequestParam(defaultValue = "startDate") String sortBy,
            @Parameter(description = "Sort direction: asc or desc", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("[REST] GET /student/{} active={} page={} size={} sortBy={} sortDir={}",
                studentId, active, page, size, sortBy, sortDir);

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(servicePort.getLoansByStudentId(studentId, active, pageable)
                .map(restMapper::toResponse));
    }
}
