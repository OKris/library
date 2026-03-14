package com.example.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Borrow {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private LocalDate borrowedAt;
    private LocalDate returnedAt;

    private LocalDate dueDate;
    @Column(name = "late_fee", columnDefinition = "DOUBLE PRECISION DEFAULT 0")
    private double lateFee;
}