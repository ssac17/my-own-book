package com.myownbook.api.repository;

import com.myownbook.api.dto.BookResponseDTO;
import com.myownbook.api.model.Book;
import com.myownbook.api.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

    @Query("select count(b) from Book b where b.title = :title")
    Integer checkTitleDuplication(@Param("title") String title);

    Optional<Book> findByIsbn(String isbn);

    @Query("select new com.myownbook.api.dto.BookResponseDTO(" +
            "b.id, b.title, b.author, b.isbn, b.publicationDate, b.category, b.recommend, " +
            "b.image.id, b.image.imagePath, b.image.thumbnailPath, " +
            "b.users.id, b.users.username, b.users.role) " +
            "from Book b " +
            "where " +
            "   (:title is null or :title = '' or b.title like %:title%) and" +
            "   (:author is null or :author = '' or b.author like %:author%) and" +
            "   (:category is null or b.category = :category) and" +
            "   (:recommend is null or b.recommend = :recommend)")
    Page<BookResponseDTO> searchAllBooksAsDto(@Param("title") String title,
                                            @Param("author") String author,
                                            @Param("category") Category category,
                                            @Param("recommend") Byte recommend,
                                            Pageable pageable);
}
