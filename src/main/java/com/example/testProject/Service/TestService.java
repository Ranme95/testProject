package com.example.testProject.Service;

import com.example.testProject.Entity.Test;
import com.example.testProject.Handler.ImageHandler;
import com.example.testProject.Repository.TestRepository;
import com.example.testProject.dto.ResponseDto;
import com.example.testProject.dto.TestDto;
import com.example.testProject.dto.UpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    ImageHandler imageHandler = new ImageHandler();

    public Test doIt(TestDto testDto) throws IOException {

        String imageFileName = imageHandler.save(testDto.getImage());

        Test test = Test.builder()
                .userId(testDto.getUserId())
                .uploadPath(imageFileName)
                .userPassword(testDto.getUserPassword())
                .build();

        return testRepository.save(test);

    }

    public ResponseDto createHome(Long id) {
        return testRepository.findById(id).map(test -> {
            return ResponseDto.builder()
                    .id(id)
                    .userId(test.getUserId())
                    .image("/product-images/" + test.getUploadPath())
                    .build();
        }).orElseThrow(() -> new RuntimeException("찾을 수 없음"));
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    public Test update(UpdateDto updateDto) throws IOException {
        Optional<Test> optionalTest = testRepository.findById(updateDto.getId());
        if (optionalTest.isEmpty()) throw new RuntimeException("해당 유저를 찾을 수 없음");

        Test test = optionalTest.get();

        if (updateDto.getUpdateImage().isEmpty()) {
            Test savedTest = Test.builder()
                    .id(updateDto.getId())
                    .uploadPath(test.getUploadPath())
                    .userId(updateDto.getUserId())
                    .userPassword(test.getUserPassword())
                    .build();
            return testRepository.save(savedTest);
        } else {
            String imageFileName = imageHandler.save(updateDto.getUpdateImage());
            Test savedTest = Test.builder()
                    .id(updateDto.getId())
                    .userId(updateDto.getUserId())
                    .uploadPath(imageFileName)
                    .userPassword(test.getUserPassword())
                    .build();
            return testRepository.save(savedTest);
        }
    }

}
