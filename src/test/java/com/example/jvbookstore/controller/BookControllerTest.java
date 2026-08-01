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

import com.example.jvbookstore.dto.book.BookDto;
import com.example.jvbookstore.dto.book.CreateBookRequestDto;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.service.BookService;
import com.example.jvbookstore.util.TestUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    private BookDto bookDto;
    private CreateBookRequestDto requestDto;

    @BeforeEach
    void setUp() {
        bookDto = TestUtil.getBookDto();
        requestDto = TestUtil.getCreateBookRequestDto();
    }

    @Test
    @DisplayName("GET /books returns a page of books")
    @WithMockUser(roles = "USER")
    void getAllBooks_booksExist_returnsPageOfBooks() throws Exception {
        Page<BookDto> page = new PageImpl<>(List.of(bookDto));
        when(bookService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", is("Dune")));
    }

    @Test
    @DisplayName("GET /books/{id} returns the book when it exists")
    @WithMockUser(roles = "USER")
    void getBookById_existingId_returnsBook() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(bookDto);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Dune")));
    }

    @Test
    @DisplayName("GET /books/{id} returns 404 when the book does not exist")
    @WithMockUser(roles = "USER")
    void getBookById_missingId_returnsNotFound() throws Exception {
        when(bookService.getBookById(99L))
                .thenThrow(new EntityNotFoundException("Book not found with id 99"));

        mockMvc.perform(get("/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /books creates a new book and returns 201")
    @WithMockUser(roles = "ADMIN")
    void createBook_validRequest_returnsCreatedBook() throws Exception {
        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(bookDto);

        mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Dune")));
    }

    @Test
    @DisplayName("POST /books returns 400 when the request is invalid")
    @WithMockUser(roles = "ADMIN")
    void createBook_invalidRequest_returnsBadRequest() throws Exception {
        CreateBookRequestDto invalidDto = new CreateBookRequestDto();

        mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /books/{id} updates the book and returns the updated dto")
    @WithMockUser(roles = "ADMIN")
    void updateBook_existingId_returnsUpdatedBook() throws Exception {
        when(bookService.update(anyLong(), any(CreateBookRequestDto.class))).thenReturn(bookDto);

        mockMvc.perform(put("/books/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Dune")));
    }

    @Test
    @DisplayName("DELETE /books/{id} deletes the book and returns 204")
    @WithMockUser(roles = "ADMIN")
    void deleteBook_existingId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }
}
