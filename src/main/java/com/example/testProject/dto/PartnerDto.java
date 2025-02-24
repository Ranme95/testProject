package com.example.testProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDto {

    @Pattern(regexp = "^[a-z]{1}[a-z0-9]{5,10}$"
            ,message = "아이디는 영문자와 숫자조합으로 5~11자리여야합니다.")
    @NotBlank(message = "아이디는 필수 정보입니다.")
    private String partnerId;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$"
            ,message = "비밀번호는 영대소문자, 숫자, 특수문자조합으로 8~15자리여야합니다.")
    @NotBlank(message = "비밀번호는 필수 정보입니다.")
    private String partnerPassword;

}
