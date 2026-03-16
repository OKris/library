package com.example.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String genre;
    private String author;
    private int year;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Borrow> borrows;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    public Book(String name, String genre, String author, int year) {
        this.name = name;
        this.genre = genre;
        this.author = author;
        this.year = year;
    }
}
