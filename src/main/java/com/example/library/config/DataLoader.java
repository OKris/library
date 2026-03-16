package com.example.library.config;


import com.example.library.entity.Book;
import com.example.library.entity.Person;
import com.example.library.entity.Role;
import com.example.library.repository.BookRepository;
import com.example.library.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {

        if (bookRepository.count() == 0) {

            List<Book> books = List.of(
                    new Book("Harry Potter and the Philosopher's Stone", "fantasy", "J. K. Rowling", 1997),
                    new Book("Harry Potter and the Chamber of Secrets", "fantasy", "J. K. Rowling", 1998),
                    new Book("Harry Potter and the Prisoner of Azkaban", "fantasy", "J. K. Rowling", 1999),
                    new Book("Harry Potter and the Goblet of Fire", "fantasy", "J. K. Rowling", 2000),
                    new Book("Harry Potter and the Order of the Phoenix", "fantasy", "J. K. Rowling", 2003),
                    new Book("Harry Potter and the Half-Blood Prince", "fantasy", "J. K. Rowling", 2005),
                    new Book("Harry Potter and the Deathly Hallows", "fantasy", "J. K. Rowling", 2007),
                    new Book("The Hobbit", "fantasy", "J. R. R. Tolkien", 1937),
                    new Book("The Fellowship of the Ring", "fantasy", "J. R. R. Tolkien", 1954),
                    new Book("The Two Towers", "fantasy", "J. R. R. Tolkien", 1954),
                    new Book("The Return of the King", "fantasy", "J. R. R. Tolkien", 1955),
                    new Book("A Game of Thrones", "fantasy", "George R. R. Martin", 1996),
                    new Book("A Clash of Kings", "fantasy", "George R. R. Martin", 1998),
                    new Book("A Storm of Swords", "fantasy", "George R. R. Martin", 2000),
                    new Book("A Feast for Crows", "fantasy", "George R. R. Martin", 2005),
                    new Book("A Dance with Dragons", "fantasy", "George R. R. Martin", 2011),
                    new Book("The Name of the Wind", "fantasy", "Patrick Rothfuss", 2007),
                    new Book("The Wise Man's Fear", "fantasy", "Patrick Rothfuss", 2011),
                    new Book("The Final Empire", "fantasy", "Brandon Sanderson", 2006),
                    new Book("The Well of Ascension", "fantasy", "Brandon Sanderson", 2007),
                    new Book("The Hero of Ages", "fantasy", "Brandon Sanderson", 2008),
                    new Book("The Eye of the World", "fantasy", "Robert Jordan", 1990),
                    new Book("The Great Hunt", "fantasy", "Robert Jordan", 1990),
                    new Book("The Dragon Reborn", "fantasy", "Robert Jordan", 1991),
                    new Book("The Gunslinger", "fantasy", "Stephen King", 1982),
                    new Book("The Drawing of the Three", "fantasy", "Stephen King", 1987),
                    new Book("The Stand", "horror", "Stephen King", 1978),
                    new Book("It", "horror", "Stephen King", 1986),
                    new Book("Dune", "science fiction", "Frank Herbert", 1965),
                    new Book("Dune Messiah", "science fiction", "Frank Herbert", 1969),
                    new Book("Children of Dune", "science fiction", "Frank Herbert", 1976),
                    new Book("Foundation", "science fiction", "Isaac Asimov", 1951),
                    new Book("Foundation and Empire", "science fiction", "Isaac Asimov", 1952),
                    new Book("Second Foundation", "science fiction", "Isaac Asimov", 1953),
                    new Book("1984", "dystopian", "George Orwell", 1949),
                    new Book("Animal Farm", "political satire", "George Orwell", 1945),
                    new Book("Brave New World", "dystopian", "Aldous Huxley", 1932),
                    new Book("Fahrenheit 451", "dystopian", "Ray Bradbury", 1953),
                    new Book("The Catcher in the Rye", "literary fiction", "J. D. Salinger", 1951),
                    new Book("To Kill a Mockingbird", "literary fiction", "Harper Lee", 1960),
                    new Book("The Great Gatsby", "literary fiction", "F. Scott Fitzgerald", 1925),
                    new Book("Moby-Dick", "adventure", "Herman Melville", 1851),
                    new Book("Pride and Prejudice", "romance", "Jane Austen", 1813),
                    new Book("The Chronicles of Narnia: The Lion, the Witch and the Wardrobe", "fantasy", "C. S. Lewis", 1950),
                    new Book("The Shining", "horror", "Stephen King", 1977),
                    new Book("Dracula", "horror", "Bram Stoker", 1897),
                    new Book("Frankenstein", "gothic fiction", "Mary Shelley", 1818),
                    new Book("The Hitchhiker's Guide to the Galaxy", "science fiction", "Douglas Adams", 1979),
                    new Book("The Road", "post-apocalyptic", "Cormac McCarthy", 2006),
                    new Book("The Book Thief", "historical fiction", "Markus Zusak", 2005)
            );
            bookRepository.saveAll(books);
            System.out.println("Loaded " + books.size() + " books into the database.");

            Person admin = new Person(
                    "admin",
                    "admin",
                    "admin@admin",
                    passwordEncoder.encode("admin"),
                    Role.ADMIN
            );
            personRepository.save(admin);
            System.out.println("Admin user created: " + admin.getEmail());
        } else {
            System.out.println("Skipping dataloader");
        }

    }
}
