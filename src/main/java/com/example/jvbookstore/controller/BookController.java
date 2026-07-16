package com.example.jvbookstore.controller;

import com.example.jvbookstore.dto.book.BookDto;
import com.example.jvbookstore.dto.book.CreateBookRequestDto;
import com.example.jvbookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get all books")
    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by id", description = "Get a book by id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create new book", description = "Create new book")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Update book")
    public BookDto updateBook(@PathVariable Long id,
                              @Valid @RequestBody CreateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }
}
