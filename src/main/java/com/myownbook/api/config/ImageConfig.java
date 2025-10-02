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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Configuration
public class ImageConfig {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Value("${upload.image.path}")
    private String imageDirPath;

    @Value("${upload.thumbnail.path}")
    private String thumbnailDirPath;

    private final String IMAGE_BASE_URL = "/static/image/";
    private final String THUMBNAIL_BASE_URL = "/static/thumbnail/";

    @PostConstruct
    public void makeImageDir() {
        try {
            Path imagePath = Paths.get(imageDirPath).toAbsolutePath().normalize();
            Path thumbnailPath = Paths.get(thumbnailDirPath).toAbsolutePath().normalize();
            if(Files.notExists(imagePath)) {
                Files.createDirectories(imagePath);
                log.info("makeImageDir = {}", imagePath);
            }
            if(Files.notExists(thumbnailPath)) {
                Files.createDirectories(thumbnailPath);
                log.info("makeImageDir = {}", thumbnailPath);
            }
        }catch (Exception e) {
            System.err.println("[makeImageDir Error] 폴더 생성 실패 " + e.getMessage());
        }
    }

    public String save(MultipartFile image) {
        String imageName = image.getOriginalFilename();
        String fileExtension = "";
        int dotIndex = imageName.lastIndexOf('.');
        if(dotIndex > 0) {
            fileExtension = imageName.substring(dotIndex);
        }
        String savedImageName = imageName.replace(fileExtension, "") + "_" + UUID.randomUUID() + fileExtension;
        String localImagePath = imageDirPath + File.separator + savedImageName;
        try {
            image.transferTo(new File(localImagePath));
            log.info("이미지 저장 성공: {}", localImagePath);
            return IMAGE_BASE_URL + savedImageName;
        }catch (IOException e) {
            log.error("ImageConfig.save Error = {}", e.getMessage(), e);
            throw new RuntimeException("이미지 파일 저장 실패", e);
        }
    }
}
