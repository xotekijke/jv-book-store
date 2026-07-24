package com.example.jvbookstore.service.impl;

import com.example.jvbookstore.dto.book.BookDto;
import com.example.jvbookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.jvbookstore.dto.book.CreateBookRequestDto;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.mapper.BookMapper;
import com.example.jvbookstore.model.Book;
import com.example.jvbookstore.model.Category;
import com.example.jvbookstore.repository.BookRepository;
import com.example.jvbookstore.repository.CategoryRepository;
import com.example.jvbookstore.service.BookService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        book.setCategories(resolveCategories(requestDto.getCategoryIds()));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository
                .findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
    }

    @Override
    @Transactional
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        bookMapper.updateBookFromDto(requestDto, book);
        book.setCategories(resolveCategories(requestDto.getCategoryIds()));

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    private Set<Category> resolveCategories(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(categoryRepository.findAllById(categoryIds));
    }
}
