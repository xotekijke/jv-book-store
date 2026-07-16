package com.example.jvbookstore.service;

import com.example.jvbookstore.dto.book.BookDto;
import com.example.jvbookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteBook(Long id);
}
