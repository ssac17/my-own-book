package com.myownbook.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class BookSearchCondition {
    private String title;
    private String author;
    private String category;

    @Min(value = 0, message = "최소 0점부터 입력이 가능합니다.")
    @Max(value = 5, message = "최대 5점까지 입력이 가능합니다.")
    private byte recommend;

    public BookSearchCondition() {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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
        return "BookSearchCondition{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", recommend=" + recommend +
                '}';
    }
}
