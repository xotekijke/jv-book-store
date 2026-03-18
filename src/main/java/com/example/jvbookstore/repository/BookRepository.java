package com.example.jvbookstore.repository;

import com.example.jvbookstore.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> getBookById(Long id);

    List<Book> findAll();

}
