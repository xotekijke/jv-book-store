package com.example.jvbookstore.repository;

import com.example.jvbookstore.model.Book;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {
    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Book save(Book book) {
        return null;
    }
}
