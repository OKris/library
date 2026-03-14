package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    Book book1 = new Book();
    Book book2= new Book();
    Book book3 = new Book();

    Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book1.setId(1L);
        book1.setName("The Hobbit");
        book1.setGenre("fantasy");
        book1.setAuthor("J. R. R. Tolkien");
        book1.setYear(1937);
        book1.setBorrows(List.of());
        book1.setAvailable(true);

        book2.setId(2L);
        book2.setName("The Two Towers");
        book2.setGenre("fantasy");
        book2.setAuthor("J. R. R. Tolkien");
        book2.setYear(1954);
        book2.setBorrows(List.of());
        book2.setAvailable(true);

        book3.setId(3L);
        book3.setName("It");
        book3.setGenre("horror");
        book3.setAuthor("Stephen King");
        book3.setYear(1986);
        book3.setBorrows(List.of());
        book3.setAvailable(true);
    }

    @Test
    void findAllByGenre () {
        Page<Book> bookPage = new PageImpl<>(List.of(book1));
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookDto> result = bookService.findAllByGenre(null, null, pageable);

        assertEquals(1, result.getContent().size());
        BookDto dto = result.getContent().getFirst();
        assertEquals("The Hobbit", dto.name());
    }

    @Test
    void findAllByGenreWithGenre () {
        Page<Book> bookPage = new PageImpl<>(List.of(book1, book2, book3));
        when(bookRepository.findByGenre("fantasy", pageable)).thenReturn(bookPage);

        Page<BookDto> result = bookService.findAllByGenre("fantasy", null, pageable);

        List<BookDto> filtered = result.getContent().stream()
                .filter(dto -> "fantasy".equals(dto.genre()))
                .toList();

        assertEquals(2, filtered.size());
        BookDto dto1 = filtered.getFirst();
        assertEquals("The Hobbit", dto1.name());

        BookDto dto2 = filtered.get(1);
        assertEquals("The Two Towers", dto2.name());
    }

    @Test
    void findAllByGenreWithSearch () {
        Page<Book> bookPage = new PageImpl<>(List.of(book3));
        when(bookRepository.findByNameContainingIgnoreCase("it", pageable)).thenReturn(bookPage);

        Page<BookDto> result = bookService.findAllByGenre(null, "it", pageable);

        assertEquals(1, result.getContent().size());
        BookDto dto = result.getContent().getFirst();
        assertEquals("It", dto.name());
    }
}