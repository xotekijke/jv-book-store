package com.example.jvbookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Book {
    @Id
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
