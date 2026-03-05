package com.example.jvbookstore.service;

import com.example.jvbookstore.dto.BookDto;
import com.example.jvbookstore.dto.CreateBookRequestDto;
import com.example.jvbookstore.mapper.BookMapper;
import com.example.jvbookstore.model.Book;
import com.example.jvbookstore.repository.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        book.setPrice(BigDecimal.valueOf(new Random().nextInt(100)));
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::toBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long id) {
        return null;
    }

    @Override
    public List<BookDto> getAllByTitle(String title) {
        return bookRepository.findAllByTitle(title)
                .stream().map(bookMapper::toBookDto)
                .collect(Collectors.toList());
    }
}
