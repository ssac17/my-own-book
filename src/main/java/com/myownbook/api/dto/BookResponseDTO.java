package com.myownbook.api.dto;

import com.myownbook.api.model.Category;
import com.myownbook.api.model.RoleEnum;
import org.springframework.hateoas.RepresentationModel;

public class BookResponseDTO extends RepresentationModel<BookResponseDTO> {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String publicationDate;
    private Category category;
    private byte recommend;
    private UserDTO user;

    public BookResponseDTO() {}

    public BookResponseDTO(Long id, String title, String author, String isbn, String publicationDate, Category category, byte recommend, Long userId, String username, RoleEnum role) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.category = category;
        this.recommend = recommend;
        this.user = new UserDTO(userId, username, role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BookResponseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", category=" + category +
                ", recommend=" + recommend +
                ", user=" + user +
                '}';
    }
}
