package com.example.library.service;

import com.example.library.dto.BorrowDto;
import com.example.library.entity.Book;
import com.example.library.entity.Borrow;
import com.example.library.entity.Person;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRepository;
import com.example.library.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class BorrowServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private BorrowRepository borrowRepository;

    @InjectMocks
    private BorrowService borrowService;

    private Book book;
    private Book book2;
    private Person person;
    private Borrow borrow;
    private Borrow borrow2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setName("Dune");
        book.setAvailable(true);

        book2 = new Book();
        book2.setId(2L);
        book2.setName("Atlantis");
        book2.setAvailable(true);

        person = new Person();
        person.setId(1L);
        person.setEmail("admin@admin.com");

        borrow = new Borrow();
        borrow.setId(1L);
        borrow.setBook(book);
        borrow.setPerson(person);
        borrow.setBorrowedAt(LocalDate.now().minusDays(5));
        borrow.setDueDate(LocalDate.now().plusDays(2));
        borrow.setReturnedAt(null);
        borrow.setLateFee(0);

        borrow2 = new Borrow();
        borrow2.setBook(book2);
        borrow2.setPerson(person);
        borrow2.setBorrowedAt(LocalDate.now().minusDays(10));
        borrow2.setDueDate(LocalDate.now().minusDays(2));
        borrow2.setReturnedAt(LocalDate.now().minusDays(1));
        borrow2.setLateFee(0.5);
    }

    @Test
    void borrowedBooksPerson() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Borrow> borrowPage = new PageImpl<>(List.of(borrow, borrow2));

        when(borrowRepository.findAllByPersonId(1L, pageable)).thenReturn(borrowPage);

        Page<BorrowDto> result = borrowService.getBorrowedBooksForPerson(1L, pageable);

        assertEquals(2, result.getContent().size());

        BorrowDto dto1 = result.getContent().getFirst();
        assertEquals(1L, dto1.bookId());
        assertEquals("Dune", dto1.title());
        assertEquals(1L, dto1.personId());
        assertNull(dto1.returnedAt());
        assertEquals(0, dto1.lateFee());

        BorrowDto dto2 = result.getContent().get(1);
        assertEquals(2L, dto2.bookId());
        assertEquals("Atlantis", dto2.title());
        assertEquals(1L, dto2.personId());
        assertEquals(0.5, dto2.lateFee());

    }

    @Test
    void borrowBook () {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(borrowRepository.existsByBookIdAndPersonId(1L, 1L)).thenReturn(false);
        when(borrowRepository.existsByBookIdAndReturnedAtIsNull(1L)).thenReturn(false);
        when(borrowRepository.save(any(Borrow.class))).thenReturn(borrow);

        Borrow result = borrowService.borrowBook(1L, 1L);

        assertEquals(book, result.getBook());
        assertEquals(person, result.getPerson());
        assertEquals(LocalDate.now(), result.getBorrowedAt());
        assertEquals(LocalDate.now().plusDays(2), result.getDueDate());
        assertFalse(book.getAvailable());
    }

    @Test
    void borrowBookAlreadyBorrowed () {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(borrowRepository.existsByBookIdAndPersonId(1L, 1L)).thenReturn(true);
        when(borrowRepository.existsByBookIdAndReturnedAtIsNull(1L)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> borrowService.borrowBook(1L, 1L));

        assertEquals("Currently borrowed by this person", exception.getMessage());
    }


    @Test
    void returnBook() {
        when(borrowRepository.findByPerson_IdAndBook_IdAndReturnedAtIsNull(1L, 1L)).thenReturn(borrow);
        when(borrowRepository.save(any(Borrow.class))).thenReturn(borrow);

        Borrow result = borrowService.returnBook(1L, 1L);

        assertEquals(0, result.getLateFee());
        assertTrue(result.getBook().getAvailable());
    }

    @Test
    void returnBookLate() {
        borrow.setDueDate(LocalDate.now().minusDays(2));

        when(borrowRepository.findByPerson_IdAndBook_IdAndReturnedAtIsNull(1L, 1L)).thenReturn(borrow);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(borrowRepository.save(any(Borrow.class))).thenReturn(borrow);

        Borrow result = borrowService.returnBook(1L, 1L);

        assertNotNull(result.getReturnedAt());
        assertTrue(result.getLateFee() > 0);
        assertEquals(1, result.getLateFee());
        assertTrue(result.getBook().getAvailable());
    }

}