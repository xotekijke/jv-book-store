package com.example.jvbookstore.service;

import com.example.jvbookstore.dto.BookDto;
import com.example.jvbookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);
}
