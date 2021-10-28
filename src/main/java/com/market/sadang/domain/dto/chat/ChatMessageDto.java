package com.market.sadang.domain.dto.chat;

import com.market.sadang.domain.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    private ChatMessage.MessageType types;
    private String roomId;
    private String sender;
    private String message;

}
