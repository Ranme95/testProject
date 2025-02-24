package com.example.testProject.Handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

//클래스를 스프링 빈으로 등록하는 어노테이션
//스프링 컨테이너가 애플리케이션을 시작할 때 해당 클래스를 인스턴스화하고 관리
@Component
public class ImageHandler {

    @Value("${image.path}")
    private String imagePath;

    @Value("${image.path.directory}")
    private String imagePathDirectory;

    public UUID saveImage(MultipartFile image) throws IOException {
        //UUID 생성
        UUID uuid = UUID.randomUUID();

        String imageName = image.getOriginalFilename();

        //이미지파일 이름 생성
        String imageFileName = uuid + "_" + imageName;

        //이미지 경로
        String uploadPath = imagePath + imageFileName;

        Path path = Paths.get(uploadPath);
        //이미지 경로인 폴더가 없으면 생성
        Files.createDirectories(path.getParent());
        //이미지 저장

        try {
            Files.write(path, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패");
        }

        return uuid;
    }

    public String getImagePath(UUID uuid, String imageName){
        return imagePathDirectory+uuid+"_"+imageName;
    }

}
