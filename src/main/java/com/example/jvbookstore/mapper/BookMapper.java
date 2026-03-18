package com.example.jvbookstore.mapper;

import com.example.jvbookstore.config.MapperConfig;
import com.example.jvbookstore.dto.BookDto;
import com.example.jvbookstore.dto.CreateBookRequestDto;
import com.example.jvbookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
