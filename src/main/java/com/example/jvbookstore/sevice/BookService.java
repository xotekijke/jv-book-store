package com.example.jvbookstore.sevice;

import com.example.jvbookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List findAll();
}
