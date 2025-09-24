package com.myownbook.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class BookSearchCondition {
    @Schema(description = "제목으로 조회", example = "채식", required = false)
    private String title;

    @Schema(description = "저자로 조회", example = "게이고", required = false)
    private String author;

    @Schema(description = "카테고리로 조회", example = "ESSAY", required = false)
    private String category;

    @Min(value = 0, message = "최소 0점부터 입력이 가능합니다.")
    @Max(value = 5, message = "최대 5점까지 입력이 가능합니다.")
    @Schema(description = "별점으로 조회", example = "5", required = false)
    private Byte recommend;

    public BookSearchCondition() {
    }

    public String getTitle() {
        return title;
    }

    public BookSearchCondition setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public BookSearchCondition setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public BookSearchCondition setCategory(String category) {
        this.category = category;
        return this;
    }

    public Byte getRecommend() {
        return recommend;
    }

    public BookSearchCondition setRecommend(Byte recommend) {
        this.recommend = recommend;
        return this;
    }

    @Override
    public String toString() {
        return "BookSearchCondition{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", recommend=" + recommend +
                '}';
    }
}
