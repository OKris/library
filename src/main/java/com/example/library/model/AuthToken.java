package com.example.library.model;

import lombok.Data;

@Data
public class AuthToken {
    private String token;
    private Long expiration;
}
