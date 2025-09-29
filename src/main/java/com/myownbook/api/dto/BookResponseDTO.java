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
    private ImageDTO image;
    private UserDTO user;

    public BookResponseDTO() {}

    public BookResponseDTO(Long id, String title, String author, String isbn, String publicationDate, Category category, byte recommend, Long imageId, String imagePath, String  thumbnailPath,  Long userId, String username, RoleEnum role) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.category = category;
        this.recommend = recommend;
        this.image = new ImageDTO(imageId, imagePath, thumbnailPath);
        this.user = new UserDTO(userId, username, role);
    }

    public Long getId() {
        return id;
    }

    public BookResponseDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BookResponseDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public BookResponseDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookResponseDTO setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public BookResponseDTO setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public BookResponseDTO setCategory(Category category) {
        this.category = category;
        return this;
    }

    public byte getRecommend() {
        return recommend;
    }

    public BookResponseDTO setRecommend(byte recommend) {
        this.recommend = recommend;
        return this;
    }

    public ImageDTO getImage() {
        return image;
    }

    public BookResponseDTO setImage(ImageDTO image) {
        this.image = image;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public BookResponseDTO setUser(UserDTO user) {
        this.user = user;
        return this;
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
                ", image=" + image +
                ", user=" + user +
                '}';
    }
}
