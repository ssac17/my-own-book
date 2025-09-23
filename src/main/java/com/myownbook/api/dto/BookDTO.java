package com.myownbook.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.ISBN;

public class BookDTO {
    @NotBlank(message = "제목은 필수 입니다.")
    @Schema(description = "도서 제목", example = "다정한 사람이 이긴다")
    private String title;

    @NotBlank(message = "저자은 필수 입니다.")
    @Schema(description = "저자", example = "이해인")
    private String author;

    @ISBN(message = "13자리의 isbn형식을 입력해 주세요.")
    @Schema(description = "isbn 코드", example = "979-119-326-265-8")
    private String isbn;

    @Pattern(regexp = "^\\d{4}$", message = "'yyyy' 형식으로 년도만 입력해 주세요.")
    @Schema(description = "출판일자", example = "2025")
    private String publicationDate;

    @Schema(description = "카테고리", example = "ESSAY")
    private String category;

    @Min(value = 0, message = "최소 0점부터 입력이 가능합니다.")
    @Max(value = 5, message = "최대 5점까지 입력이 가능합니다.")
    @Schema(description = "추천 별점 0 ~ 5", example = "4")
    private Byte recommend;

    public String getTitle() {
        return title;
    }

    public BookDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public BookDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookDTO setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public BookDTO setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public BookDTO setCategory(String category) {
        this.category = category;
        return this;
    }

    public Byte getRecommend() {
        return recommend;
    }

    public BookDTO setRecommend(byte recommend) {
        this.recommend = recommend;
        return this;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationDate=" + publicationDate +
                ", category=" + category +
                ", recommend=" + recommend +
                '}';
    }
}
