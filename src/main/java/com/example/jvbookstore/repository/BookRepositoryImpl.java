package com.example.jvbookstore.repository;

import com.example.jvbookstore.model.Book;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {
    private BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
