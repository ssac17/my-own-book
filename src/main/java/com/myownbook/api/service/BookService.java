package com.myownbook.api.service;

import com.myownbook.api.dto.BookResponseDTO;
import com.myownbook.api.dto.BookSearchCondition;
import com.myownbook.api.dto.UserDTO;
import com.myownbook.api.model.Book;
import com.myownbook.api.dto.BookDTO;
import com.myownbook.api.model.Category;
import com.myownbook.api.model.User;
import com.myownbook.api.repository.BookRepository;
import com.myownbook.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class BookService {

    private BookRepository repository;
    private UserRepository userRepository;
    private Logger log = LoggerFactory.getLogger(getClass());

    public BookService(BookRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public BookResponseDTO insert(BookDTO bookDTO) {
        Book newBook = new Book();
        BeanUtils.copyProperties(bookDTO, newBook);
        newBook.setCategory(setCategory(bookDTO));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("책 등록은 로그인 후 이용해주세요");
        }
        String username = authentication.getName();
        User findUser = userRepository.findByUsername(username);
        if(Objects.isNull(findUser)) {
            throw new IllegalArgumentException("인증된 사용자 정보를 찾을 수 없습니다: " + username);
        }
        newBook.setUser(findUser);
        repository.save(newBook);
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setUser(new UserDTO(findUser.getId(), findUser.getUsername(), findUser.getRole()));
        BeanUtils.copyProperties(newBook, bookResponseDTO);
        return bookResponseDTO;
    }

    public Page<BookResponseDTO> searchAll(BookSearchCondition condition) {
        log.info("condition = {}", condition);
        String titleParam = StringUtils.hasText(condition.getTitle()) ? condition.getTitle() : null;
        String authorParam = StringUtils.hasText(condition.getAuthor()) ? condition.getAuthor() : null;
        Category categoryParam = null;
        if(StringUtils.hasText(condition.getCategory())) {
            categoryParam = Category.valueOf(condition.getCategory().toUpperCase());
        }
        Byte recommendParam = condition.getRecommend();
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        return repository.searchAllBooksAsDto(titleParam, authorParam, categoryParam, recommendParam, pageRequest);
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
        if (bookDTO.getTitle() != null) {
            findBook.setTitle(bookDTO.getTitle());
        }
        if (bookDTO.getAuthor() != null) {
            findBook.setAuthor(bookDTO.getAuthor());
        }
        if (bookDTO.getIsbn() != null) { // ISBN 같은 필드들도 null 체크
            findBook.setIsbn(bookDTO.getIsbn());
        }
        if (bookDTO.getPublicationDate() != null) {
            findBook.setPublicationDate(bookDTO.getPublicationDate());
        }
        if (bookDTO.getCategory() != null) {
            findBook.setCategory(Category.valueOf(bookDTO.getCategory()));
        }
        if (bookDTO.getRecommend() != null) { // Byte로 바꿨으니 null 체크 가능
            findBook.setRecommend(bookDTO.getRecommend());
        }
        return repository.save(findBook);
    }

    public String deleteBook(Long id) {
        Book findBook = findBook(id);
        repository.deleteById(id);
        return findBook.getTitle();
    }

    private Category setCategory(BookDTO bookDTO) {
        //중목 제목 확인
        Integer duplicationChecked = repository.checkTitleDuplication(bookDTO.getTitle());
        if(duplicationChecked > 0) {
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
}