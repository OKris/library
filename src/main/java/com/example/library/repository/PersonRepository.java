package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    
    List<Book> findByFavourites(Long personId);

    Person findByEmail(String email);
}
