package com.example.library.controller;

import com.example.library.dto.BorrowDto;
import com.example.library.entity.Borrow;
import com.example.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BorrowController {

    @Autowired
    BorrowService borrowService;


    @GetMapping("books/borrowed/{personId}")
    public Page<BorrowDto> getBorrowedBooksForPerson (@PathVariable Long personId, Pageable pageable) {
        return borrowService.getBorrowedBooksForPerson(personId, pageable);
    }

    @GetMapping("books/borrowed")
    public List<BorrowDto> getBorrowedBooks () {
        return borrowService.getBorrowedBooks();
    }


    @PostMapping("books/borrow")
    public Borrow borrowBook (@RequestParam Long id, @RequestParam Long personId) {
        return borrowService.borrowBook(id, personId);
    }

    @PostMapping("books/borrow-multiple")
    public List<Borrow> borrowManyBooks (@RequestParam Long personId, @RequestBody List<Long> bookIds) {
        return borrowService.borrowManyBooks(personId, bookIds);
    }

    @PostMapping("books/return")
    public Borrow returnBook (@RequestParam Long bookId, @RequestParam Long personId) {
       return borrowService.returnBook(bookId, personId);
    }
}
