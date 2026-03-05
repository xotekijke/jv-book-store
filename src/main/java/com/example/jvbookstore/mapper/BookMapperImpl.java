package com.example.jvbookstore.mapper;

import com.example.jvbookstore.dto.BookDto;
import com.example.jvbookstore.dto.CreateBookRequestDto;
import com.example.jvbookstore.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto toBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        return bookDto;
    }

    @Override
    public Book toModel(CreateBookRequestDto requestDto) {
        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        return book;
    }
}
