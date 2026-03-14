package com.example.library.controller;

import com.example.library.dto.BookDto;
import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("books")
    public Page<BookDto> findAllByGenre (@RequestParam(required = false) String genre, @RequestParam(required = false)  String search, Pageable pageable) {
        return bookService.findAllByGenre(genre, search, pageable);
    }

    @GetMapping("genres")
    public List<String> getGenres () {
        return bookRepository.findAllGenres();
    }

    @GetMapping("book/{id}")
    public BookDto findABook (@PathVariable Long id) {
        return bookService.findABook(id);
    }

    @PostMapping("save-book")
    public Book save (@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PostMapping("save-books")
    public List<Book> saveBooks (@RequestBody List<Book> books) {
        return bookRepository.saveAll(books);
    }

    @PutMapping("book")
    public Book editBook(@RequestBody Book book) {
        if(book.getId()==null) {
            throw new RuntimeException("Cannot edit book without id");
        }
        return bookRepository.save(book);
    }

    @DeleteMapping("delete")
    public List<Book> delete (@RequestParam Long id) {
        bookRepository.deleteById(id);
        return bookRepository.findAll();
    }

}
