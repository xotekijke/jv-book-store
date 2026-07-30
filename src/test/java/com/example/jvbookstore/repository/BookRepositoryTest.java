package com.example.jvbookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.jvbookstore.model.Book;
import com.example.jvbookstore.model.Category;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category fictionCategory;
    private Category scienceCategory;
    private Book book;

    @BeforeEach
    void setUp() {
        fictionCategory = new Category();
        fictionCategory.setName("Fiction");
        fictionCategory.setDescription("Fiction books");
        fictionCategory = categoryRepository.save(fictionCategory);

        scienceCategory = new Category();
        scienceCategory.setName("Science");
        scienceCategory.setDescription("Science books");
        scienceCategory = categoryRepository.save(scienceCategory);

        book = new Book();
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");
        book.setIsbn("978-0441172719");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("A science fiction novel");

        Set<Category> categories = new HashSet<>();
        categories.add(fictionCategory);
        book.setCategories(categories);

        book = bookRepository.save(book);
    }

    @Test
    @DisplayName("findAllByCategoryId() returns books that belong to the given category")
    void findAllByCategoryId_bookLinkedToCategory_returnsBook() {
        List<Book> books = bookRepository.findAllByCategoryId(fictionCategory.getId());

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Dune");
        assertThat(books.get(0).getIsbn()).isEqualTo("978-0441172719");
    }

    @Test
    @DisplayName("findAllByCategoryId() returns empty list when no book belongs to category")
    void findAllByCategoryId_noBooksLinkedToCategory_returnsEmptyList() {
        List<Book> books = bookRepository.findAllByCategoryId(scienceCategory.getId());

        assertThat(books).isEmpty();
    }

    @Test
    @DisplayName("save() persists a book and generates an id")
    void save_validBook_persistsBookWithGeneratedId() {
        Book newBook = new Book();
        newBook.setTitle("1984");
        newBook.setAuthor("George Orwell");
        newBook.setIsbn("978-0451524935");
        newBook.setPrice(BigDecimal.valueOf(9.99));

        Book savedBook = bookRepository.save(newBook);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(bookRepository.findById(savedBook.getId())).isPresent();
    }

    @Test
    @DisplayName("findById() does not return a book that has been soft deleted")
    void findById_softDeletedBook_returnsEmpty() {
        Long id = book.getId();

        bookRepository.deleteById(id);

        assertThat(bookRepository.findById(id)).isEmpty();
    }
}
