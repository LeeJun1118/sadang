package com.market.sadang.domain.dto;

import com.market.sadang.domain.Board;
import lombok.Getter;

@Getter
public class BoardListResponseDto {
    private Long id;
    private String member;
    private String title;
    private Long thumbnailId;

    public BoardListResponseDto(Board entity){
        this.id = entity.getId();
        this.member = entity.getMember().getUserId();
        this.title = entity.getTitle();

        //첨부파일이 존재하면
        if (!entity.getFileList().isEmpty()){
            // 첫번째 이미지 반환
            this.thumbnailId = entity.getFileList().get(0).getId();
        }
        else{
            // 서버에 저장된 기본 이미지 반환환
           this.thumbnailId = 0L;
        }
    }
}
