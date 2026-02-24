package com.example.jvbookstore;

import com.example.jvbookstore.model.Book;
import com.example.jvbookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class JvBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(JvBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book schoolbook = new Book();
            schoolbook.setTitle("Math");
            schoolbook.setAuthor("Professor");
            schoolbook.setPrice(BigDecimal.valueOf(200));
            schoolbook.setDescription("9th grade");
            schoolbook.setIsbn("978-3-16-148410-0");

            bookService.save(schoolbook);

            System.out.println(bookService.findAll());
        };
    }
}
