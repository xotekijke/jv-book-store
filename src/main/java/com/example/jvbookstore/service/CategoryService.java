package com.example.jvbookstore.service;

import com.example.jvbookstore.dto.category.CategoryDto;
import com.example.jvbookstore.dto.category.CreateCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryDto categoryDto);

    CategoryDto update(Long id, CreateCategoryDto categoryDto);

    void deleteById(Long id);
}
