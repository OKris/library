package com.example.library.dto;

import lombok.Data;

import java.util.List;

public record BookDto(
    Long id,
    String name,
    String genre,
    String author,
    int year,
    List<BorrowStatusDto> activeBorrows,
    Boolean available
){}
