package com.myownbook.api.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class ImageConfig {

    private Logger log = LoggerFactory.getLogger(getClass());

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

    public String save(MultipartFile image) {
        String imageName = image.getOriginalFilename();
        String pathName = imageDir + "/" +imageName;
        try {
            image.transferTo(new File(pathName));
        }catch (IOException e) {
            log.error("ImageConfig.save Error = {}", e.getMessage());
        }
        return pathName;
    }
}
