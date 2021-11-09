package com.market.sadang.dto.bord;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardMemberDto {
    private Long id;
    private Member member;
    private String title;

    public BoardMemberDto(Board entity){
        this.id = entity.getId();
        this.member = entity.getMember();
        this.title = entity.getTitle();
    }
}
