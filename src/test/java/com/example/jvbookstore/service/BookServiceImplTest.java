package com.example.jvbookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.jvbookstore.dto.book.BookDto;
import com.example.jvbookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.jvbookstore.dto.book.CreateBookRequestDto;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.mapper.BookMapper;
import com.example.jvbookstore.model.Book;
import com.example.jvbookstore.model.Category;
import com.example.jvbookstore.repository.BookRepository;
import com.example.jvbookstore.repository.CategoryRepository;
import com.example.jvbookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;
    private CreateBookRequestDto requestDto;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fiction");

        book = new Book();
        book.setId(1L);
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");
        book.setIsbn("978-0441172719");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setCategories(Set.of(category));

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Dune");
        bookDto.setAuthor("Frank Herbert");
        bookDto.setIsbn("978-0441172719");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        bookDto.setCategoryIds(Set.of(1L));

        requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Dune");
        requestDto.setAuthor("Frank Herbert");
        requestDto.setIsbn("978-0441172719");
        requestDto.setPrice(BigDecimal.valueOf(19.99));
        requestDto.setCategoryIds(Set.of(1L));
    }

    @Test
    @DisplayName("save() maps the request, resolves categories and persists the book")
    void save_validRequestDto_returnsSavedBookDto() {
        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(requestDto.getCategoryIds()))
                .thenReturn(List.copyOf(book.getCategories()));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.save(requestDto);

        assertThat(actual).isEqualTo(bookDto);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("save() sets an empty category set when no category ids are provided")
    void save_noCategoryIds_savesBookWithEmptyCategories() {
        requestDto.setCategoryIds(null);
        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        bookService.save(requestDto);

        verify(categoryRepository, never()).findAllById(any());
    }

    @Test
    @DisplayName("findAll() returns a page of book dtos")
    void findAll_validPageable_returnsPageOfBookDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actual = bookService.findAll(pageable);

        assertThat(actual.getContent()).hasSize(1);
        assertThat(actual.getContent().get(0)).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("getBookById() returns the book dto when the book exists")
    void getBookById_existingId_returnsBookDto() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.getBookById(1L);

        assertThat(actual).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("getBookById() throws EntityNotFoundException when the book does not exist")
    void getBookById_missingId_throwsEntityNotFoundException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("update() updates an existing book and returns the updated dto")
    void update_existingId_returnsUpdatedBookDto() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(categoryRepository.findAllById(requestDto.getCategoryIds()))
                .thenReturn(List.copyOf(book.getCategories()));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.update(1L, requestDto);

        assertThat(actual).isEqualTo(bookDto);
        verify(bookMapper).updateBookFromDto(requestDto, book);
    }

    @Test
    @DisplayName("update() throws EntityNotFoundException when the book does not exist")
    void update_missingId_throwsEntityNotFoundException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(99L, requestDto))
                .isInstanceOf(EntityNotFoundException.class);
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteBook() deletes the book when it exists")
    void deleteBook_existingId_deletesBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteBook() does nothing when the book does not exist")
    void deleteBook_missingId_doesNotCallDelete() {
        when(bookRepository.existsById(99L)).thenReturn(false);

        bookService.deleteBook(99L);

        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("findAllByCategoryId() returns books mapped without category ids")
    void findAllByCategoryId_existingCategory_returnsBookList() {
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setId(1L);
        dto.setTitle("Dune");
        when(bookRepository.findAllByCategoryId(1L)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto);

        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(1L);

        assertThat(actual).containsExactly(dto);
    }
}
