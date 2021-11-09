package com.market.sadang.dto.chat;

import com.market.sadang.domain.ChatMessage;
import com.market.sadang.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ChatMessageListTimeDto {
    private String roomId;
    private String sender;
    private String message;
    private String createdDate;

    @Builder
    public ChatMessageListTimeDto(ChatMessage entity) {
        this.roomId = entity.getRoomId();
        this.sender = entity.getSender().getUsername();
        this.message = entity.getMessage();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        this.createdDate = entity.getCreatedDate().format(formatter);
    }
}
