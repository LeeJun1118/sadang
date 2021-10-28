package com.market.sadang.dto.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "이름을 입력해주세요")
    private String name;

    @NotEmpty(message = "아이디를 입력해주세요")
    private String username;

    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;

    @NotEmpty(message = "주소를 입력해주세요")
    private String address;

    @NotEmpty(message = "상세 주소를 입력해주세요")
    private String detailAddress;
}
