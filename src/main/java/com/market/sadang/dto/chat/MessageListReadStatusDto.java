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
    private String buyerName;
    private String sellerName;
    private String boardTitle;
//    private int countReadStatus;

    private int countSenderReadStatus;
    private int countReceiverReadStatus;

    @Builder
    public MessageListReadStatusDto(ChatRoom entity/*, int countReadStatus*/, int countSenderReadStatus, int countReceiverReadStatus) {
        this.roomId = entity.getRoomId();
        this.buyerName = entity.getBuyerName();
        this.sellerName = entity.getSellerName();
        this.boardTitle = entity.getBoardTitle();
//        this.countReadStatus = countReadStatus;
        this.countSenderReadStatus = countSenderReadStatus;
        this.countReceiverReadStatus = countReceiverReadStatus;
    }
}
