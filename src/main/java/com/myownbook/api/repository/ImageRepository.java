package com.myownbook.api.repository;

import com.myownbook.api.model.BookImage;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<BookImage, Long> {
}
