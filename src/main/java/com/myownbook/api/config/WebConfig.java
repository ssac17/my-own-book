package com.myownbook.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.image.path}")
    private String imageDirPath;

    @Value("${upload.thumbnail.path}")
    private String thumbnailDirPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path imageUploadPath = Paths.get(imageDirPath).toAbsolutePath().normalize();
        // Windows 호환성
        String imageFileLocation = "file:" + imageUploadPath.toString().replace("\\", "/") + "/";
        registry.addResourceHandler("/static/image/**").addResourceLocations(imageFileLocation);

        Path thumbnailUploadPath = Paths.get(thumbnailDirPath).toAbsolutePath().normalize();
        // Windows 호환성
        String thumbnailFileLocation = "file:" + thumbnailUploadPath.toString().replace("\\", "/") + "/";
        registry.addResourceHandler("/static/thumbnail/**").addResourceLocations(thumbnailFileLocation);

        registry.addResourceHandler("/static/**").addResourceLocations("classpath:static/");
    }
}
