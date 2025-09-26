package com.myownbook.api.service;

import com.myownbook.api.dto.BookResponseDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceCachingTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private CacheManager cacheManager;

    //테스트전 캐시 비우기
    @BeforeEach
    void setUp() {
        if(cacheManager.getCache("books") != null) {
            cacheManager.getCache("books").clear();
            System.out.println("캐시 초기화 완료");
        }
    }
    
    @Test
    @Transactional
    @DisplayName("findById 호출 - redis 성능 확인")
    void findById_redis() {
        //첫번째 호출
        long firstStartTime = System.nanoTime();
        Long bookId = 1L;
        BookResponseDTO firstResult = bookService.findById(bookId);
        long firstEndTime = System.nanoTime();
        long firstElapseTime = (firstEndTime - firstStartTime) / 1_000_000;

        //두번째 호출(캐시)
        long secondStartTime = System.nanoTime();
        BookResponseDTO secondResult = bookService.findById(bookId);
        long secondEndTime = System.nanoTime();
        long secondElapseTime = (secondEndTime - secondStartTime) / 1_000_000;

        System.out.println("findById 첫번째 호출 시간: " + firstElapseTime);
        System.out.println("findById 두번째 호출 시간: " + secondElapseTime);

        assertThat(firstResult).isNotNull();
        assertThat(secondResult).isNotNull();
        assertThat(firstResult.getId()).isEqualTo(bookId);
        assertThat(secondResult.getId()).isEqualTo(bookId);
        assertTrue(secondElapseTime < firstElapseTime - 3, "두번째 캐시 호출이 첫번째 DB 호출보다 최소 3ms 이상 빨라야 합니다.");
    }

    @Test
    @Transactional
    @DisplayName("findByIsbn 호출 - redis 성능 확인")
    void findByIsbn_redis() {
        //첫번째 호출
        long firstStartTime = System.nanoTime();
        String bookIsbn = "978-899-098-257-5";
        BookResponseDTO firstResult = bookService.findByIsbn(bookIsbn);
        long firstEndTime = System.nanoTime();
        long firstElapseTime = (firstEndTime - firstStartTime) / 1_000_000;

        //두번째 호출(캐시)
        long secondStartTime = System.nanoTime();
        BookResponseDTO secondResult = bookService.findByIsbn(bookIsbn);
        long secondEndTime = System.nanoTime();
        long secondElapseTime = (secondEndTime - secondStartTime) / 1_000_000;

        System.out.println("findByIsbn 첫번째 호출 시간: " + firstElapseTime);
        System.out.println("findByIsbn 두번째 호출 시간: " + secondElapseTime);

        assertThat(firstResult).isNotNull();
        assertThat(secondResult).isNotNull();
        assertThat(firstResult.getIsbn()).isEqualTo(bookIsbn);
        assertThat(secondResult.getIsbn()).isEqualTo(bookIsbn);
        assertTrue(secondElapseTime < firstElapseTime - 3, "두번째 캐시 호출이 첫번째 DB 호출보다 최소 3ms 이상 빨라야 합니다.");

    }
}