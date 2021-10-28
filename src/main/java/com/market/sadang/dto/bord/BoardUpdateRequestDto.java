package com.market.sadang.dto.bord;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    private Long id;
    private String title;
    private String content;
    private String price;

    @Builder
    public BoardUpdateRequestDto(Long id, String title,String price, String content) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.content = content;
    }
}
