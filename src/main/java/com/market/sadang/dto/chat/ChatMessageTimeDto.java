package com.market.sadang.dto.chat;

import com.market.sadang.domain.ChatMessage;
import com.market.sadang.domain.ReadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ChatMessageTimeDto {
    private Long id;

    private ChatMessage.MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String receiver; // 메시지 받는 사람
    private String message; // 메시지
    private ReadStatus senderStatus;
    private ReadStatus receiverStatus;
    private String createdDate;

    @Builder
    public ChatMessageTimeDto(ChatMessage entity) {
        this.roomId = entity.getRoomId();
        this.sender = entity.getSender().getUsername();
        this.message = entity.getMessage();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        this.createdDate = entity.getCreatedDate().format(formatter);
    }
}
