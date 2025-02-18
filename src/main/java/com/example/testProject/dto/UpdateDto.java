package com.example.testProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {

    @Pattern(regexp = "^[a-z]{1}[a-z0-9]{5,10}$"
            ,message = "아이디는 영문자와 숫자조합으로 5~10자리여야합니다.")
    @NotBlank(message = "아이디는 필수 정보입니다.")
    private String userId;

    private Long id;

    private MultipartFile updateImage;

}
