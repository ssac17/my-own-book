package com.myownbook.api.service;

import com.myownbook.api.dto.BookSearchCondition;
import com.myownbook.api.model.Book;
import com.myownbook.api.model.BookDTO;
import com.myownbook.api.model.Category;
import com.myownbook.api.repository.BookRepository;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public Page<Book> all(BookSearchCondition condition) {
        return searchBook(condition);
    }

    public Book findById(Long id) {
        return findBook(id);
    }

    public Book findByIsbn(String isbn) {
        Book findBook = repository.findByIsbn(isbn);
        if(Objects.isNull(findBook)) {
            throw new IllegalArgumentException("해당 isbn의 도서가 존재하지 않습니다.");
        }
        return findBook;
    }

    public Book updateBook(Long id, BookDTO bookDTO) {
        Book findBook = findBook(id);
        BeanUtils.copyProperties(bookDTO, findBook);
        return repository.save(findBook);
    }

    public String deleteBook(Long id) {
        Book findBook = findBook(id);
        repository.deleteById(id);
        return findBook.getTitle();
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

    private Book findBook(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id의 도서가 존재하지 않습니다."));
    }

    private Page<Book> searchBook(BookSearchCondition condition) {
        log.info("condition = {}", condition);
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        if(Strings.isNotEmpty(condition.getTitle())) {
            return repository.findByTitleContaining(condition.getTitle(), pageRequest);
        }
        if(Strings.isNotEmpty(condition.getAuthor())) {
            return repository.findByAuthorContaining(condition.getAuthor(), pageRequest);
        }
        if(Strings.isNotEmpty(condition.getCategory())) {
            Category categoryValue = Category.valueOf(condition.getCategory().toUpperCase());
            return repository.findByCategoryLike(categoryValue, pageRequest);
        }
        byte recommend = condition.getRecommend();
        if(recommend >= 0 && recommend <= 5) {
            return repository.findByRecommendGreaterThanEqual(recommend, pageRequest);
        }
        return repository.findAll(pageRequest);
    }
}