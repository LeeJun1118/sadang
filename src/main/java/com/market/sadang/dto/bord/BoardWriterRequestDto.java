package com.market.sadang.dto.bord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
public class BoardWriterRequestDto {
    private String boardId;
}
