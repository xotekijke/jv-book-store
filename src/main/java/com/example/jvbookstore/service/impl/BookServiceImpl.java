package com.example.jvbookstore.service.impl;

import com.example.jvbookstore.dto.book.BookDto;
import com.example.jvbookstore.dto.book.CreateBookRequestDto;
import com.example.jvbookstore.dto.user.UserResponseDto;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.mapper.BookMapper;
import com.example.jvbookstore.model.Book;
import com.example.jvbookstore.repository.BookRepository;
import com.example.jvbookstore.service.BookService;
import com.example.jvbookstore.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserService userService;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository
                .findAll(pageable)
                .map(bookMapper::toBookDto);
    }

    @Override
    public Page<BookDto> findAll(String email, Pageable pageable) {
        UserResponseDto userResponseDto = userService.getByEmail(email);
        List<BookDto> bookDtos = bookRepository.findAllByUserId(userResponseDto.getId())
                .stream()
                .map(bookMapper::toDto)
                .toList();
        return new PageImpl<>(bookDtos, pageable, bookDtos.size());
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookDto)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
    }

    @Override
    @Transactional
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        bookMapper.updateBookFromDto(requestDto, book);

        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }
    }
}
