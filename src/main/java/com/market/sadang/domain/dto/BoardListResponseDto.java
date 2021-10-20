package com.market.sadang.domain.dto;

import com.market.sadang.domain.Board;
import lombok.Getter;

@Getter
public class BoardListResponseDto {
    private Long id;
    private String member;
    private String title;

    public BoardListResponseDto(Board entity){
        this.id = entity.getId();
        this.member = entity.getMember().getUserId();
        this.title = entity.getTitle();
    }
}
