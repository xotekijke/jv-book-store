package com.example.jvbookstore.repository;

import com.example.jvbookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

}
