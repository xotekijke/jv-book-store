package com.example.jvbookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.jvbookstore.model.Category;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");
        category = categoryRepository.save(category);
    }

    @Test
    @DisplayName("save() persists a category and generates an id")
    void save_validCategory_persistsCategoryWithGeneratedId() {
        Category newCategory = new Category();
        newCategory.setName("Horror");
        newCategory.setDescription("Horror books");

        Category saved = categoryRepository.save(newCategory);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Horror");
    }

    @Test
    @DisplayName("findById() returns the persisted category")
    void findById_existingCategory_returnsCategory() {
        List<Category> all = categoryRepository.findAll();

        assertThat(all).hasSize(1);
        assertThat(categoryRepository.findById(category.getId()))
                .isPresent()
                .get()
                .extracting(Category::getName)
                .isEqualTo("Fantasy");
    }

    @Test
    @DisplayName("findById() does not return a category that has been soft deleted")
    void findById_softDeletedCategory_returnsEmpty() {
        Long id = category.getId();

        categoryRepository.deleteById(id);

        assertThat(categoryRepository.findById(id)).isEmpty();
        assertThat(categoryRepository.findAll()).isEmpty();
    }
}
