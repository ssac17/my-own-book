package com.myownbook.api.dto;

import com.myownbook.api.model.Book;

public class ImageDTO {
    private Long id;
    private String imagePath;
    private String thumbnailPath;

    public ImageDTO() {}

    public ImageDTO(Long id, String imagePath, String thumbnailPath) {
        this.id = id;
        this.imagePath = imagePath;
        this.thumbnailPath = thumbnailPath;
    }
    public ImageDTO(Book book) {
        this.id = book.getImage().getId();
        this.imagePath = book.getImage().getImagePath();
        this.thumbnailPath = book.getImage().getThumbnailPath();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}

