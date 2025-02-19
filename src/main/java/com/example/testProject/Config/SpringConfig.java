package com.example.testProject.Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

    @Value("${image.path}")
    private String imagePath;

    @Value("${image.path.directory}")
    private String imagePathDirectory;

    /**
     * 이미지 경로 외부로 설정
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(imagePathDirectory+"**").addResourceLocations("file:///"+imagePath);
    }
}
