package com.example.jvbookstore.repository;

import com.example.jvbookstore.model.Book;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

}
