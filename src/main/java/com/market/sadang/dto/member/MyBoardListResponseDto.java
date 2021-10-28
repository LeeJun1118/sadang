package com.market.sadang.dto.member;

import com.market.sadang.domain.Board;
import lombok.Getter;

@Getter
public class MyBoardListResponseDto {
    private Long id;
    private String member;
    private String title;
    private String price;
    private Long thumbnailId;

    public MyBoardListResponseDto(Board entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.price = entity.getPrice();

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
