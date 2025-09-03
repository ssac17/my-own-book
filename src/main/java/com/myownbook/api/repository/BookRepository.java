package com.myownbook.api.repository;

import com.myownbook.api.model.Book;
import com.myownbook.api.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

    Book findByTitle(String title);
    Book findByIsbn(String isbn);
    Page<Book> findByTitleContaining(String title, Pageable pageable);
    Page<Book> findByAuthorContaining(String author, Pageable pageable);
    Page<Book> findByCategoryLike(Category category, Pageable pageable);
    Page<Book> findByRecommendGreaterThanEqual(byte recommend, Pageable pageable);
}
