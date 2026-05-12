package com.bio_library.catalog.infrastructure.adapters.driving.rest.controller;

import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;
import com.bio_library.catalog.application.ports.in.IBookServicePort;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.request.LoanCountRequest;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response.BookResponse;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response.PageResponse;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.mapper.IBookRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book catalog queries and loan count management")
public class BookController {

    private final IBookServicePort servicePort;
    private final IBookRestMapper restMapper;

    @Operation(summary = "Get paginated list of books")
    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("[REST] GET books page={} size={}", page, size);
        PageResult<Book> result = servicePort.getBooks(page, size);
        return ResponseEntity.ok(restMapper.toPageResponse(result));
    }

    @Operation(summary = "Get book by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable String id) {
        log.info("[REST] GET book id={}", id);
        return ResponseEntity.ok(restMapper.toResponse(servicePort.getBookById(id)));
    }

    @Operation(summary = "Update active loan count (INCREMENT or DECREMENT)")
    @PatchMapping("/{id}/loan-count")
    public ResponseEntity<BookResponse> updateLoanCount(
            @PathVariable String id,
            @RequestBody @Valid LoanCountRequest request) {
        log.info("[REST] PATCH loan-count book id={} action={}", id, request.getAction());
        return ResponseEntity.ok(restMapper.toResponse(servicePort.updateLoanCount(id, request.getAction())));
    }
}
