package com.market.sadang.dto.bord;

import com.market.sadang.domain.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardMemberDto {
    private Long id;
    private String member;

    public BoardMemberDto(Board entity){
        this.id = entity.getId();
        this.member = entity.getMember().getUsername();
    }
}
