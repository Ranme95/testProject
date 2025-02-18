package com.example.testProject.Handler;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ImageHandler {

    public String save(MultipartFile image) throws IOException {
        //UUID 생성
        UUID uuid = UUID.randomUUID();

        //이미지파일 이름 생성 (UUID_원래파일이름)
        String imageFileName = uuid + "_" + image.getOriginalFilename();

        //이미지 경로
        String uploadPath = "C:/product-images/" + imageFileName;

        Path path = Paths.get(uploadPath);
        //이미지 경로인 폴더가 없으면 생성
        Files.createDirectories(path.getParent());
        //이미지 저장

        try {
            Files.write(path, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패");
        }

        return imageFileName;
    }
}
