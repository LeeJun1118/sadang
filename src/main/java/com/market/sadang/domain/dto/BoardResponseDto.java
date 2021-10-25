package com.market.sadang.domain.dto;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String member;
    private String title;
    private String content;
    private String address;
    private String price;
    private List<Long> fileIdList;

    public BoardResponseDto(Board entity, List<Long> fileIdList){
        this.id = entity.getId();
        this.member = entity.getMember().getUserId();
        this.title = entity.getTitle();
        this.price = entity.getPrice();
        this.content = entity.getContent();
        this.address = entity.getMember().getAddress();
        this.fileIdList = fileIdList;
    }
}
