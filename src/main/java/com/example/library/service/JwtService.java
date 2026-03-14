package com.example.library.service;

import com.example.library.entity.Person;
import com.example.library.model.AuthToken;
import com.example.library.repository.PersonRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Autowired
    private PersonRepository personRepository;

    String superSecretKey = "UaYR4G3DM8gLGJjCaMDswa6IaiMPqXsK66ORVcHMBaY";

    public AuthToken generateToken(Person person){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(superSecretKey));
        String token = Jwts.builder()
                .subject(person.getId().toString())
                .signWith(secretKey)
                .compact();
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        return authToken;

    }

    public Person validateToken(String token){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(superSecretKey));
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long personId = Long.valueOf(claims.getSubject());

        return personRepository.findById(personId).orElseThrow();
    }
}
