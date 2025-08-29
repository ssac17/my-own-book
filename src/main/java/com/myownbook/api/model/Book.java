package com.myownbook.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String author;

    private String isbn;

    private LocalDate publicationDate;

    private Category category;

    private byte recommend;
}
