package com.example.jvbookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.jvbookstore.dto.category.CategoryDto;
import com.example.jvbookstore.dto.category.CreateCategoryDto;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.mapper.CategoryMapper;
import com.example.jvbookstore.model.Category;
import com.example.jvbookstore.repository.CategoryRepository;
import com.example.jvbookstore.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CreateCategoryDto createCategoryDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Fiction");
        category.setDescription("Fiction books");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Fiction");
        categoryDto.setDescription("Fiction books");

        createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Fiction");
        createCategoryDto.setDescription("Fiction books");
    }

    @Test
    @DisplayName("findAll() returns a page of category dtos")
    void findAll_validPageable_returnsPageOfCategoryDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> actual = categoryService.findAll(pageable);

        assertThat(actual.getContent()).containsExactly(categoryDto);
    }

    @Test
    @DisplayName("getById() returns the category dto when the category exists")
    void getById_existingId_returnsCategoryDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getById(1L);

        assertThat(actual).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("getById() throws EntityNotFoundException when the category does not exist")
    void getById_missingId_throwsEntityNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("save() maps and persists a new category")
    void save_validRequestDto_returnsSavedCategoryDto() {
        when(categoryMapper.toEntity(createCategoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.save(createCategoryDto);

        assertThat(actual).isEqualTo(categoryDto);
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("update() updates an existing category and returns the updated dto")
    void update_existingId_returnsUpdatedCategoryDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.update(1L, createCategoryDto);

        assertThat(actual).isEqualTo(categoryDto);
        verify(categoryMapper).updateCategoryFromDto(createCategoryDto, category);
    }

    @Test
    @DisplayName("update() throws EntityNotFoundException when the category does not exist")
    void update_missingId_throwsEntityNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.update(99L, createCategoryDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("deleteById() delegates deletion to the repository")
    void deleteById_existingId_deletesCategory() {
        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(1L);
    }
}
