package com.myownbook.api.service;

import com.myownbook.api.model.Book;
import com.myownbook.api.model.BookDTO;
import com.myownbook.api.model.Category;
import com.myownbook.api.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private BookRepository repository;
    private Logger log = LoggerFactory.getLogger(getClass());

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book insert(BookDTO bookDTO) {
        Book newBook = new Book();
        BeanUtils.copyProperties(bookDTO, newBook);
        newBook.setCategory(setCategory(bookDTO));

        return repository.save(newBook);
    }

    public List<Book> all() {
        return repository.findAll();
    }


    private Category setCategory(BookDTO bookDTO) {
        //중목 제목 확인
        Book findTitle = repository.findByTitle(bookDTO.getTitle());
        if(!Objects.isNull(findTitle)) {
           throw new IllegalArgumentException("이미 등록된 제목입니다.");
        }
        Book findIsbn = repository.findByIsbn(bookDTO.getIsbn());
        if(!Objects.isNull(findIsbn)) {
            throw new IllegalArgumentException("이미 등록된 isbn입니다.");
        }
        String category = bookDTO.getCategory();
        StringBuilder categoryTypes = new StringBuilder();
        for (Category value : Category.values()) {
            categoryTypes.append(value).append(" / ");
            if(value.name().equals(category)) {
                return value;
            }
        }
        categoryTypes.delete(categoryTypes.length() - 3, categoryTypes.length());
        throw new IllegalArgumentException("유효한 카테고리를 입력해주세요 = " + "{ " + categoryTypes + " }");
    }
}
