package com.myownbook.api;

import com.myownbook.api.model.Book;
import com.myownbook.api.model.BookDTO;
import com.myownbook.api.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private BookRepository repository;
    private Logger log = LoggerFactory.getLogger(getClass());

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book insert(BookDTO bookDTO) {
        log.info("service bookDTO = {}", bookDTO);
        Book newBook = new Book();
        BeanUtils.copyProperties(bookDTO, newBook);
        log.info("service newBook = {}", newBook);
        return repository.save(newBook);
    }
}
