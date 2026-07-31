package com.example.jvbookstore.util;

import com.example.jvbookstore.dto.book.BookDto;
import com.example.jvbookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.jvbookstore.dto.book.CreateBookRequestDto;
import com.example.jvbookstore.model.Book;
import com.example.jvbookstore.model.Category;
import java.math.BigDecimal;
import java.util.Set;

public class TestUtil {

    private TestUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Category getCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fiction");
        return category;
    }

    public static Book getBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");
        book.setIsbn("978-0441172719");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setCategories(Set.of(getCategory()));
        return book;
    }

    public static BookDto getBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Dune");
        bookDto.setAuthor("Frank Herbert");
        bookDto.setIsbn("978-0441172719");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        bookDto.setCategoryIds(Set.of(1L));
        return bookDto;
    }

    public static BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setId(1L);
        dto.setTitle("Dune");
        return dto;
    }

    public static CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Dune");
        requestDto.setAuthor("Frank Herbert");
        requestDto.setIsbn("978-0441172719");
        requestDto.setPrice(BigDecimal.valueOf(19.99));
        requestDto.setCategoryIds(Set.of(1L));
        return requestDto;
    }
}
