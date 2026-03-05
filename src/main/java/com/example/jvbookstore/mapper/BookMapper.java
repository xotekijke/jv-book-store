package com.example.jvbookstore.mapper;

import com.example.jvbookstore.dto.BookDto;
import com.example.jvbookstore.dto.CreateBookRequestDto;
import com.example.jvbookstore.model.Book;

public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
