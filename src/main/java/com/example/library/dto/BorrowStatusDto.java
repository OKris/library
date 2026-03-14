package com.example.library.dto;

import java.time.LocalDate;

public record BorrowStatusDto(
        Long personId,
        LocalDate borrowedAt,
        LocalDate returnedAt
) {
}
