package com.market.sadang.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class SignUpForm {

    @NotEmpty(message = "id를 입력해주세요")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;

    @NotEmpty(message = "주소를 입력해주세요")
    private String address;
}
