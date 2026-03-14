package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.dto.BorrowStatusDto;
import com.example.library.entity.Book;
import com.example.library.entity.Borrow;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


    public Page<BookDto> findAllByGenre (@RequestParam(required = false) String genre, @RequestParam(required = false)  String search, Pageable pageable) {
        Page<Book> booksPage;

        if (search != null && !search.isEmpty()) {
            booksPage = bookRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (genre != null && !genre.isEmpty()) {
            booksPage = bookRepository.findByGenre(genre, pageable);
        } else {
            booksPage = bookRepository.findAll(pageable);
        }

        return booksPage.map(book -> {
            Optional<Borrow> activeBorrow = book.getBorrows().stream()
                    .filter(b -> b.getReturnedAt() == null)
                    .findFirst();

            BorrowStatusDto borrowStatus = activeBorrow
                    .map(b -> new BorrowStatusDto(
                            b.getPerson().getId(),
                            b.getBorrowedAt(),
                            b.getReturnedAt()
                    ))
                    .orElse(null);

            List<BorrowStatusDto> activeBorrowsList = borrowStatus != null ? List.of(borrowStatus) : List.of();

            return new BookDto(
                    book.getId(),
                    book.getName(),
                    book.getGenre(),
                    book.getAuthor(),
                    book.getYear(),
                    activeBorrowsList,
                    book.getAvailable()
            );
        });
    }

    public BookDto findABook (@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        Optional<Borrow> activeBorrow = book.getBorrows().stream()
                .filter(b -> b.getReturnedAt() == null)
                .findFirst();
        BorrowStatusDto borrowStatus = activeBorrow
                .map(b -> new BorrowStatusDto(
                        b.getPerson().getId(),
                        b.getBorrowedAt(),
                        b.getReturnedAt()
                ))
                .orElse(null);

        List<BorrowStatusDto> activeBorrowsList = borrowStatus != null ? List.of(borrowStatus) : List.of();
        return new BookDto(
                book.getId(),
                book.getName(),
                book.getGenre(),
                book.getAuthor(),
                book.getYear(),
                activeBorrowsList,
                book.getAvailable()
        );
    }
}
