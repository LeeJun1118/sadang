package com.market.sadang.dto.member;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyChatRoomListResponseDto {
    private Long id;
    private String roomId;
    private String seller;
    private String buyer;
    private Long boardId;

    @Builder
    public MyChatRoomListResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.seller = chatRoom.getSeller().getUsername();
        this.buyer = chatRoom.getBuyer().getUsername();
        this.boardId = chatRoom.getBoardId();
    }
}
