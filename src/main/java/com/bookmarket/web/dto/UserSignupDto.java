package com.bookmarket.web.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupDto {

    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상으로 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
    private String passwordConfirm;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    private String phone;

    @NotBlank(message = "우편번호는 필수 항목입니다.")
    private String postcode;

    @NotBlank(message = "주소는 필수 항목입니다.")
    private String address;

    @AssertTrue(message = "이용약관에 동의해야 합니다.")
    private boolean agreeTerms;
}
