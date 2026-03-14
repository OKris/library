package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.dto.PersonDto;
import com.example.library.entity.Book;
import com.example.library.entity.Person;
import com.example.library.entity.Role;
import com.example.library.repository.BookRepository;
import com.example.library.repository.PersonRepository;
import org.apache.catalina.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.autoconfigure.JdbcProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private PersonService personService;

    Person person = new Person();
    Person person2 = new Person();
    Book book1 = new Book();
    PersonDto personDto = new PersonDto();
    PersonDto person2Dto = new PersonDto();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        person.setId(1L);
        person.setEmail("anna@sky");

        book1.setId(3L);
        book1.setName("It");
        book1.setGenre("horror");
        book1.setAuthor("Stephen King");
        book1.setYear(1986);
        book1.setBorrows(List.of());
        book1.setAvailable(true);

        personDto.setId(1L);
        personDto.setEmail("anna@sky");

        /*person2.setId(2L);
        person2.setEmail("elsa@ee");
        person2.setPassword("password");

        person2Dto.setId(2L);
        person2Dto.setEmail("elsa@ee");*/

    }

    //@Test
    /*void save() {
        when(encoder.encode("password")).thenReturn("encodedPassword");

        //person2.setPassword("encodedPassword");
        Person savedPerson = new Person();
        savedPerson.setId(2L);
        savedPerson.setEmail("elsa@ee");
        savedPerson.setPassword("encodedPassword");
        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);
        when(mapper.map(savedPerson, PersonDto.class)).thenReturn(person2Dto);

        PersonDto result = personService.save(person2);

        assertEquals(2L, result.getId());
        assertEquals("elsa@ee", result.getEmail());
        assertEquals("encodedPassword", person2.getPassword());

        verify(encoder, times(1)).encode("password");
        verify(personRepository, times(1)).save(any(Person.class));
        verify(mapper, times(1)).map(savedPerson, PersonDto.class);
    }*/


    @Test
    void getProfile() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("1");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(mapper.map(person, PersonDto.class)).thenReturn(personDto);

        PersonDto result = personService.getProfile();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("anna@sky", result.getEmail());

        verify(personRepository, times(1)).findById(1L);
        verify(mapper, times(1)).map(person, PersonDto.class);

    }

    @Test
    void getProfileNoToken() {
        SecurityContextHolder.clearContext();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> personService.getProfile());
        assertEquals("No authentication found", exception.getMessage());
    }

    @Test
    void updateFavourites() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(bookRepository.findAllById(Collections.singletonList(3L)))
                .thenReturn(Collections.singletonList(book1));

        when(personRepository.save(person)).thenReturn(person);

        Person result = personService.updateFavourites(1L, Collections.singletonList(3L));

        assertEquals(1, result.getFavourites().size());
        Book books = result.getFavourites().getFirst();
        assertEquals(3L, books.getId());
    }

}