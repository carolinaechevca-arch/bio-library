package com.bio_library.catalog.infrastructure.adapters.driving.rest.controller;

import com.bio_library.catalog.domain.enums.Category;
import com.bio_library.catalog.domain.model.Book;
import com.bio_library.catalog.domain.model.PageResult;
import com.bio_library.catalog.application.ports.in.IBookServicePort;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.request.BookRequest;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.request.LoanCountRequest;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response.BookResponse;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response.PageResponse;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.mapper.IBookRestMapper;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book catalog management")
public class BookController {

    private final IBookServicePort servicePort;
    private final IBookRestMapper restMapper;

    @Operation(summary = "Create a new book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created", content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "A book with that ISBN already exists", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        log.info("[REST] POST books isbn={}", request.isbn());
        Book saved = servicePort.createBook(restMapper.toBook(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toResponse(saved));
    }

    @Operation(summary = "List books paginated, optionally filtered by category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated list of books", content = @Content(schema = @Schema(implementation = PageResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getBooks(
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("[REST] GET books category={} page={} size={}", category, page, size);
        PageResult<Book> result = servicePort.getBooks(category, page, size);
        return ResponseEntity.ok(restMapper.toPageResponse(result));
    }

    @Operation(summary = "List all available categories")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of category enum values")
    })
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(servicePort.getCategories());
    }

    @Operation(summary = "Find book by ISBN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found", content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
        log.info("[REST] GET book isbn={}", isbn);
        return ResponseEntity.ok(restMapper.toResponse(servicePort.getBookByIsbn(isbn)));
    }

    @Operation(summary = "Get book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found", content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable String id) {
        log.info("[REST] GET book id={}", id);
        return ResponseEntity.ok(restMapper.toResponse(servicePort.getBookById(id)));
    }

    @Operation(summary = "Update book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated", content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "ISBN already used by another book", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "422", description = "totalLicenses is lower than current active loans", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable String id,
            @Valid @RequestBody BookRequest request) {
        log.info("[REST] PUT book id={} isbn={}", id, request.isbn());
        Book updated = servicePort.updateBook(id, restMapper.toBook(request));
        return ResponseEntity.ok(restMapper.toResponse(updated));
    }

    @Operation(summary = "Delete book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        log.info("[REST] DELETE book id={}", id);
        servicePort.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update active loan count (INCREMENT or DECREMENT)", description = "INCREMENT: new loan — decrements availableLicenses. DECREMENT: return — increments availableLicenses.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "License count updated", content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "422", description = "No licenses available (INCREMENT) or all licenses already free (DECREMENT)", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{id}/loan-count")
    public ResponseEntity<BookResponse> updateLoanCount(
            @PathVariable String id,
            @RequestBody @Valid LoanCountRequest request) {
        log.info("[REST] PATCH loan-count book id={} action={}", id, request.getAction());
        return ResponseEntity.ok(restMapper.toResponse(servicePort.updateLoanCount(id, request.getAction())));
    }
}
