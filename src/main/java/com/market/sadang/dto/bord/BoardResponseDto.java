package com.market.sadang.dto.bord;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.BoardStatus;
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
    private BoardStatus status;
    private List<Long> fileIdList;

    public BoardResponseDto(Board entity, List<Long> fileIdList){
        this.id = entity.getId();
        this.member = entity.getMember().getUsername();
        this.title = entity.getTitle();
        this.price = entity.getPrice();
        this.content = entity.getContent();
        this.address = entity.getMember().getAddress();
        this.status = entity.getSellStatus();
        this.fileIdList = fileIdList;
    }
}
