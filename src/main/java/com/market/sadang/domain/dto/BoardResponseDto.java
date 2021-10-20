package com.market.sadang.domain.dto;

import com.market.sadang.domain.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String member;
    private String title;
    private String content;

    public BoardResponseDto(Board entity){
        this.id = entity.getId();
        this.member = entity.getMember().getUserId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
    }
}
