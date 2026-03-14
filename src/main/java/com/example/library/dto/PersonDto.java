package com.example.library.dto;

import com.example.library.entity.Book;
import com.example.library.entity.Borrow;
import com.example.library.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Data
public class PersonDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
