package com.market.sadang.dto.chat;

import com.market.sadang.domain.ChatMessage;
import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.ReadStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto implements Serializable {

    private ChatMessage.MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String receiver;
    private ReadStatus senderStatus;
    private ReadStatus receiverStatus;
    private String createdDate;



    @Builder
    public ChatMessageDto(ChatMessage entity) {
        this.type = entity.getType();
        this.roomId = entity.getRoomId();
        this.sender = entity.getSender().getUsername();
        this.message = entity.getMessage();
        this.receiver = entity.getReceiver().getUsername();
        this.senderStatus = entity.getSenderStatus();
        this.receiverStatus = entity.getReceiverStatus();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        this.createdDate = entity.getCreatedDate().format(formatter);

    }
}
