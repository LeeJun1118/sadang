package com.market.sadang.domain.dto.myFile;


import com.market.sadang.domain.MyFile;
import lombok.Getter;

@Getter
public class MyFileResponseDto {
    private Long fileId;  // 파일 id

    public MyFileResponseDto(MyFile entity){
        this.fileId = entity.getId();
    }
}
