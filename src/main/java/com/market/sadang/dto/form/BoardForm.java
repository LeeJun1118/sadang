package com.market.sadang.dto.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BoardForm {
    @NotEmpty(message = "제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요")
    private String content;

    @NotEmpty(message = "가격을 입력해주세요")
    private String price;
}
