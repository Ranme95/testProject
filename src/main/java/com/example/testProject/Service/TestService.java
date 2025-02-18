package com.example.testProject.Service;

import com.example.testProject.Entity.Test;
import com.example.testProject.Handler.ImageHandler;
import com.example.testProject.Repository.TestRepository;
import com.example.testProject.dto.ResponseDto;
import com.example.testProject.dto.TestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    ImageHandler imageHandler = new ImageHandler();

    public Test doIt(TestDto testDto) throws IOException {

        String imageFileName = imageHandler.save(testDto.getImage());

        Test test = Test.builder()
                        .uploadPath(imageFileName)
                        .build();

        return testRepository.save(test);

    }

    public ResponseDto createHome(Long id) {
      return testRepository.findById(id).map(test -> {
          return ResponseDto.builder()
                          .image("/product-images/"+test.getUploadPath())
                          .build();
        }).orElseThrow(()->new RuntimeException("찾을 수 없음"));
    }

}
