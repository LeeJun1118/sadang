package com.market.sadang.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    private Long id;
    private String title;
    private String content;

    @Builder
    public BoardUpdateRequestDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
