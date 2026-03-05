package com.example.jvbookstore;

import com.example.jvbookstore.dto.CreateBookRequestDto;
import com.example.jvbookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
            CreateBookRequestDto schoolbook = new CreateBookRequestDto();
            schoolbook.setTitle("Math");
            schoolbook.setAuthor("Professor");

            bookService.save(schoolbook);

            System.out.println(bookService.findAll());
        };
    }
}
