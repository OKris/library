package com.example.library.repository;

import com.example.library.dto.BookDto;
import com.example.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);

    @Query("SELECT DISTINCT book.genre FROM Book book")
    List<String> findAllGenres();

    Page<Book> findByGenre(String genre, Pageable pageable);

    Page<Book> findByNameContainingIgnoreCase(String query, Pageable pageable);

    Page<Book> findByAvailableTrue(Pageable pageable);

    Page<Book> findByAvailableFalse(Pageable pageable);
}
