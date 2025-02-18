package com.example.testProject.dto;

import com.example.testProject.validator.ValidFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestDto {

    @Pattern(regexp = "^[a-z]{1}[a-z0-9]{5,10}$"
            ,message = "아이디는 영문자와 숫자조합으로 5~11자리여야합니다.")
    @NotBlank(message = "아이디는 필수 정보입니다.")
    private String userId;

    @ValidFile(message = "이미지는 필수입니다.")
    private MultipartFile image;


}
