package com.example.library.service;

import com.example.library.dto.BorrowDto;
import com.example.library.entity.Book;
import com.example.library.entity.Borrow;
import com.example.library.entity.Person;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRepository;
import com.example.library.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class BorrowService {

    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private EmailService emailService;

    private static final double LATE_FEE_PER_DAY = 0.5;

    public Page<BorrowDto> getBorrowedBooksForPerson (@PathVariable Long personId, Pageable pageable) {
        Page<Borrow> borrowsPage = borrowRepository.findAllByPersonId(personId, pageable);

        return borrowsPage.map(b -> new BorrowDto(
                b.getBook().getId(),
                b.getBook().getName(),
                b.getPerson().getId(),
                b.getBorrowedAt(),
                b.getReturnedAt(),
                b.getDueDate(),
                b.getLateFee()
        ));
    }

    public List<BorrowDto> getBorrowedBooks () {
        return borrowRepository.findAllByReturnedAtIsNull()
                .stream()
                .map(b -> new BorrowDto(
                        b.getBook().getId(),
                        b.getBook().getName(),
                        b.getPerson().getId(),
                        b.getBorrowedAt(),
                        b.getReturnedAt(),
                        b.getDueDate(),
                        b.getLateFee()
                ))
                .toList();
    }

    public Borrow borrowBook (@RequestParam Long id, @RequestParam Long personId) {
        Book book = bookRepository.findById(id).orElseThrow();
        Person person = personRepository.findById(personId).orElseThrow();

        if (borrowRepository.existsByBookIdAndPersonId(id, personId) && borrowRepository.existsByBookIdAndReturnedAtIsNull(id)) {
            throw new RuntimeException("Currently borrowed by this person");
        }

        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setPerson(person);
        borrow.setBorrowedAt(LocalDate.now());
        borrow.setDueDate(LocalDate.now().plusDays(2));
        book.setAvailable(false);

        emailService.sendPlainText("noreply@gmail.com", "Library borrow", "Book return date is " + borrow.getDueDate());

        return borrowRepository.save(borrow);
    }

    public List<Borrow> borrowManyBooks (@RequestParam Long personId, @RequestBody List<Long> bookIds) {
        if (bookIds.isEmpty()) {
            throw new RuntimeException("No books included");
        }
        Person person = personRepository.findById(personId).orElseThrow();
        List<Borrow> borrowManyBooks = new ArrayList<>();

        for (Long bookId : bookIds) {
            Book book = bookRepository.findById(bookId).orElseThrow();

            if (borrowRepository.existsByBookIdAndPersonId(bookId, personId) && borrowRepository.existsByBookIdAndReturnedAtIsNull(bookId)) {
                throw new RuntimeException("Currently borrowed by this person " + bookId);
            }

            Borrow borrow = new Borrow();
            borrow.setBook(book);
            borrow.setPerson(person);
            borrow.setBorrowedAt(LocalDate.now());
            borrow.setDueDate(LocalDate.now().plusDays(2));
            book.setAvailable(false);
            bookRepository.save(book);

            borrowManyBooks.add(borrowRepository.save(borrow));
        }

        return borrowManyBooks;
    }

    public Borrow returnBook (@RequestParam Long bookId, @RequestParam Long personId) {
        Borrow borrow = borrowRepository.findByPerson_IdAndBook_IdAndReturnedAtIsNull(personId, bookId);

        if (borrow == null) {
            throw new RuntimeException("No active borrow found");
        }

        LocalDate today = LocalDate.now();
        borrow.setReturnedAt(today);

        if (today.isAfter(borrow.getDueDate())) {

            long daysLate = ChronoUnit.DAYS.between(borrow.getDueDate(), today);
            double fee = daysLate * LATE_FEE_PER_DAY;

            borrow.setLateFee(fee);
        }

        Book book =  borrow.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return borrowRepository.save(borrow);
    }
}
