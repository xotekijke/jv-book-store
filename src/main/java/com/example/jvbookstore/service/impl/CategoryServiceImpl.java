package com.example.jvbookstore.service.impl;

import com.example.jvbookstore.dto.category.CategoryDto;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.mapper.CategoryMapper;
import com.example.jvbookstore.model.Category;
import com.example.jvbookstore.repository.CategoryRepository;
import com.example.jvbookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(getCategoryOrThrow(id));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = getCategoryOrThrow(id);
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found with id: " + id));
    }
}
