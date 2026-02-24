package com.example.jvbookstore.service;

import com.example.jvbookstore.model.Book;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
