package com.myownbook.api.repository;

import com.myownbook.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

    Book findByTitle(String title);
    Book findByIsbn(String isbn);
}
