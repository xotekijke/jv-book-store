package com.example.jvbookstore.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.jvbookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.jvbookstore.dto.category.CategoryDto;
import com.example.jvbookstore.dto.category.CreateCategoryDto;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.security.JwtAuthenticationFilter;
import com.example.jvbookstore.service.BookService;
import com.example.jvbookstore.service.CategoryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(controllers = CategoryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private BookService bookService;

    private CategoryDto categoryDto;
    private CreateCategoryDto createCategoryDto;

    @BeforeEach
    void setUp() {
        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Fiction");
        categoryDto.setDescription("Fiction books");

        createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Fiction");
        createCategoryDto.setDescription("Fiction books");
    }

    @Test
    @DisplayName("POST /categories creates a new category and returns 201")
    void createCategory_validRequest_returnsCreatedCategory() throws Exception {
        when(categoryService.save(any(CreateCategoryDto.class))).thenReturn(categoryDto);

        mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createCategoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Fiction")));
    }

    @Test
    @DisplayName("POST /categories returns 400 when the request is invalid")
    void createCategory_invalidRequest_returnsBadRequest() throws Exception {
        CreateCategoryDto invalidDto = new CreateCategoryDto();

        mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /categories returns a page of categories")
    void getAll_categoriesExist_returnsPageOfCategories() throws Exception {
        Page<CategoryDto> page = new PageImpl<>(List.of(categoryDto));
        when(categoryService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is("Fiction")));
    }

    @Test
    @DisplayName("GET /categories/{id} returns the category when it exists")
    void getCategoryById_existingId_returnsCategory() throws Exception {
        when(categoryService.getById(1L)).thenReturn(categoryDto);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Fiction")));
    }

    @Test
    @DisplayName("GET /categories/{id} returns 404 when the category does not exist")
    void getCategoryById_missingId_returnsNotFound() throws Exception {
        when(categoryService.getById(99L))
                .thenThrow(new EntityNotFoundException("Category not found with id: 99"));

        mockMvc.perform(get("/categories/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /categories/{id} updates the category and returns the updated dto")
    void updateCategory_existingId_returnsUpdatedCategory() throws Exception {
        when(categoryService.update(anyLong(), any(CreateCategoryDto.class))).thenReturn(categoryDto);

        mockMvc.perform(put("/categories/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createCategoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Fiction")));
    }

    @Test
    @DisplayName("DELETE /categories/{id} deletes the category and returns 204")
    void deleteCategory_existingId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteById(1L);
    }

    @Test
    @DisplayName("GET /categories/{id}/books returns the books for the given category")
    void getBooksByCategoryId_existingCategory_returnsBookList() throws Exception {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(1L);
        bookDto.setTitle("Dune");
        when(bookService.findAllByCategoryId(1L)).thenReturn(List.of(bookDto));

        mockMvc.perform(get("/categories/1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Dune")));
    }
}
