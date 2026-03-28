package com.example.jvbookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateBookRequestDto {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    
    @NotBlank(message = "Author cannot be blank")
    private String author;
    
    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;
    
    @Positive(message = "Price must be positive")
    private Double price;
    
    private String description;
}
