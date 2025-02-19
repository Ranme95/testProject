package com.example.testProject.Service;

import com.example.testProject.Entity.Test;
import com.example.testProject.Entity.TestImage;
import com.example.testProject.Handler.ImageHandler;
import com.example.testProject.Repository.TestImageRepository;
import com.example.testProject.Repository.TestRepository;
import com.example.testProject.dto.ResponseDto;
import com.example.testProject.dto.TestDto;
import com.example.testProject.dto.UpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TestService {

    private final TestRepository testRepository;

    private final TestImageRepository testImageRepository;

    private final ImageHandler imageHandler;

    public Test saveTest(TestDto testDto) throws IOException {

        UUID uuid = imageHandler.save(testDto.getImage());

        Test test = Test.builder()
                .userId(testDto.getUserId())
                .userPassword(testDto.getUserPassword())
                .build();

        TestImage testImage = TestImage.builder()
                .test(test)
                .uuid(uuid)
                .imageName(testDto.getImage().getOriginalFilename())
                .build();

        test.setTestImage(testImage);

        testImageRepository.save(testImage);

        return testRepository.save(test);

    }

    public ResponseDto createHome(Long id) {
        return testRepository.findById(id).map(test -> {
            String imagePath = imageHandler.getImagePath(test.getTestImage().getUuid(), test.getTestImage().getImageName());

            return ResponseDto.builder()
                    .id(id)
                    .userId(test.getUserId())
                    .image(imagePath)
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

        Optional<TestImage> optionalTestImage = testImageRepository.findByTestId(test.getId());
        if (optionalTestImage.isEmpty()) throw new RuntimeException("해당 이미지를 찾을 수 없음");

        TestImage testImage = optionalTestImage.get();

        if (updateDto.getUpdateImage().isEmpty()) {
            Test savedTest = Test.builder()
                    .id(updateDto.getId())
                    .userId(updateDto.getUserId())
                    .userPassword(test.getUserPassword())
                    .build();

            TestImage savedTestImage = TestImage.builder()
                    .id(testImage.getId())
                    .uuid(testImage.getUuid())
                    .test(savedTest)
                    .imageName(testImage.getImageName())
                    .build();

            savedTest.setTestImage(savedTestImage);
            testImageRepository.save(savedTestImage);

            return testRepository.save(savedTest);

        } else {
            UUID uuid = imageHandler.save(updateDto.getUpdateImage());
            Test savedTest = Test.builder()
                    .id(updateDto.getId())
                    .userId(updateDto.getUserId())
                    .userPassword(test.getUserPassword())
                    .build();
            TestImage savedTestImage = TestImage.builder()
                    .id(testImage.getId())
                    .uuid(uuid)
                    .test(savedTest)
                    .imageName(updateDto.getUpdateImage().getOriginalFilename())
                    .build();
            savedTest.setTestImage(savedTestImage);
            testImageRepository.save(savedTestImage);

            return testRepository.save(savedTest);
        }
    }

}
