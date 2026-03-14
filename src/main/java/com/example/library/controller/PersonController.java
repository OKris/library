package com.example.library.controller;

import com.example.library.dto.PersonDto;
import com.example.library.dto.PersonLoginDto;
import com.example.library.entity.Book;
import com.example.library.entity.Person;
import com.example.library.model.AuthToken;
import com.example.library.repository.PersonRepository;
import com.example.library.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    ModelMapper mapper;

    @GetMapping("persons")
    public List<PersonDto> findAll () {
        return List.of(mapper.map(personRepository.findAll(), PersonDto[].class));
    }

    @PostMapping("signup")
    public PersonDto save(@RequestBody Person person){
        return personService.save(person);
    }

    @PostMapping("login")
    public AuthToken login(@RequestBody PersonLoginDto personLoginDto) {
        return personService.login(personLoginDto);
    }

    @GetMapping("profile")
    public PersonDto getProfile(){
        return personService.getProfile();
    }

    @PutMapping("update-profile")
    public PersonDto updateProfile(@RequestBody Person person) {
        Person dbPerson = personRepository.save(person);
        return mapper.map(dbPerson, PersonDto.class);
    }

    @GetMapping("favourites")
    public List<Book> getPersonsFavourites(@RequestParam Long personId) {
        Person person = personRepository.findById(personId).orElseThrow(() ->
                new RuntimeException("Person not found"));

        return person.getFavourites();
    }

    @PostMapping("favourites")
    public Person updateFavourites(@RequestParam Long personId, @RequestBody List<Long> bookIds) {
        return personService.updateFavourites(personId, bookIds);
    }
}
