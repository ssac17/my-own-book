package com.myownbook.api.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Files;

@Configuration
public class ImageConfig {

    @Value("${upload.image.path}")
    private String imageDir;

    @Value("${upload.thumbnail.path}")
    private String thumbnailDir;

    @PostConstruct
    public void makeImageDir() {
        try {
            File uploadPath = new File(imageDir);
            File thumbnailPath = new File(thumbnailDir);
            if(Files.notExists(uploadPath.toPath())) {
                uploadPath.mkdir();
            }
            if(Files.notExists(thumbnailPath.toPath())) {
                thumbnailPath.mkdir();
            }
        }catch (Exception e) {
            System.err.println("[ImageConfig makeImageDir ERROR] 폴더 생성 실패 " + e.getMessage());
        }
    }
}
