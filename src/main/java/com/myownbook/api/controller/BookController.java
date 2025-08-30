package com.myownbook.api.controller;

import com.myownbook.api.service.BookService;
import com.myownbook.api.model.Book;
import com.myownbook.api.model.BookDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookService service;
    private Logger log = LoggerFactory.getLogger(getClass());

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<Book> add(@RequestBody @Valid BookDTO bookDTO) {
        Book addBook = service.insert(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addBook);
    }

    @GetMapping("/")
    public ResponseEntity<List<Book>> findAll() {
        log.info("findAll");
        List<Book> books = service.all();
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/id/{id}")
    public Book getBook(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/isbn/{isbn}")
    public Book getBook(@PathVariable String isbn) {
        return service.findByIsbn(isbn);
    }
}
