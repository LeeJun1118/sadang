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
    private String sellerName;
    private String buyerName;
    private Long boardId;

    @Builder
    public MyChatRoomListResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.sellerName = chatRoom.getSellerName();
        this.buyerName = chatRoom.getBuyerName();
        this.boardId = chatRoom.getBoardId();
    }
}
