package com.market.sadang.domain.dto.bord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.market.sadang.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
public class BoardWriterRequestDto {
    private String boardId;
}
