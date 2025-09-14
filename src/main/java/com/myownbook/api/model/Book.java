package com.myownbook.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 20)
    private String author;

    @Column(length = 20)
    private String isbn;

    @Pattern(regexp = "^\\d{4}$", message = "'yyyy' 형식으로 년도만 입력해 주세요.")
    private String publicationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Min(value = 0, message = "최소 0점부터 입력이 가능합니다.")
    @Max(value = 5, message = "최대 5점까지 입력이 가능합니다.")
    private byte recommend = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User users;

    public User getUser() {
        return users;
    }

    public void setUser(User user) {
        this.users = user;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public byte getRecommend() {
        return recommend;
    }

    public void setRecommend(byte recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publicationDate=" + publicationDate +
                ", category=" + category +
                ", recommend=" + recommend +
                '}';
    }
}
