package com.market.sadang.dto.chat;

import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.ReadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageListReadStatusDto {

    private String roomId;
    private String buyer;
    private String seller;
    private String boardTitle;
    private int countReadStatus;
    private String lastMessageTime;

    /*private int countSenderReadStatus;
    private int countReceiverReadStatus;*/

    @Builder
    public MessageListReadStatusDto(ChatRoom entity, int countReadStatus, String lastMessageTime) {
        this.roomId = entity.getRoomId();
        this.buyer = entity.getBuyer().getUsername();
        this.seller = entity.getSeller().getUsername();
        this.boardTitle = entity.getBoardTitle();
        this.countReadStatus = countReadStatus;
        this.lastMessageTime = lastMessageTime;
       /* this.countSenderReadStatus = countSenderReadStatus;
        this.countReceiverReadStatus = countReceiverReadStatus;*/
    }
}
