package com.market.sadang.dto.bord;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.MyFile;
import lombok.Getter;

@Getter
public class BoardListResponseDto {
    private Long id;
    private String member;
    private String address;
    private String title;
    private String price;
    private Long thumbnailId;

    public BoardListResponseDto(Board entity) {
        this.id = entity.getId();
        this.member = entity.getMember().getUsername();
        this.address = entity.getMember().getAddress();
        this.title = entity.getTitle();
        this.price = entity.getPrice();

        //첨부파일이 존재하면
        if (!entity.getFileList().isEmpty()) {
            // 첫번째 이미지 반환
//            this.thumbnailId = entity.getFileList().get(0).getId();
            for (MyFile file : entity.getFileList()) {
                if (file.getFilePath().contains("thumbnail_"))
                    this.thumbnailId = file.getId();
            }
        } else {
            // 서버에 저장된 기본 이미지 반환환
            this.thumbnailId = 0L;
        }
    }
}
