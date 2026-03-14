package com.example.library.service;

import com.example.library.dto.PersonDto;
import com.example.library.dto.PersonLoginDto;
import com.example.library.entity.Book;
import com.example.library.entity.Person;
import com.example.library.model.AuthToken;
import com.example.library.repository.BookRepository;
import com.example.library.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    ModelMapper mapper;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public PersonDto save(@RequestBody Person person){
        //person.setRole(Role.USER);
        person.setPassword(encoder.encode(person.getPassword()));
        Person dbPerson = personRepository.save(person);
        return mapper.map(dbPerson, PersonDto.class);
    }

    public AuthToken login(@RequestBody PersonLoginDto personLoginDto) {
        Person dbPerson = personRepository.findByEmail(personLoginDto.email());
        if (dbPerson == null) {
            throw new RuntimeException("Invalid email");
        }
        // input password hash matches hash on db
        if (!encoder.matches(personLoginDto.password(), dbPerson.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtService.generateToken(dbPerson);
    }

    public PersonDto getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("No authentication found");
        }

        Long personId = Long.parseLong(Objects.requireNonNull(authentication.getPrincipal()).toString());
        Person dbPerson = personRepository.findById(personId).orElseThrow();
        return mapper.map(dbPerson, PersonDto.class);
    }

    public Person updateFavourites(@RequestParam Long personId, @RequestBody List<Long> bookIds) {
        Person person = personRepository.findById(personId).orElseThrow();
        List<Book> books = bookRepository.findAllById(bookIds);
        person.setFavourites(books);
        return personRepository.save(person);
    }
}
