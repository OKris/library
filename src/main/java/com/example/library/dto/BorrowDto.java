package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public record BorrowDto(
        Long bookId,
        String title,
        Long personId,
        LocalDate borrowedAt,
        LocalDate returnedAt,
        LocalDate dueDate,
        Double lateFee
) {
}
