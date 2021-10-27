package com.market.sadang.domain.dto.myFile;

import com.market.sadang.domain.MyFile;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyFileDto {
    private String originFileName;
    private String filePath;
    private Long fileSize;

    @Builder
    public MyFileDto(String originFileName, String filePath, Long fileSize) {
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public MyFile toEntity(){
        MyFile build = MyFile.builder()
                .originFileName(originFileName)
                .filePath(filePath)
                .fileSize(fileSize)
                .build();
        return build;
    }
}
