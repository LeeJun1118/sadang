package com.market.sadang.domain.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    public enum MessageType{
        ENTER, CHAT, LEAVE
    }

    private String chatRoomId;
    private String writer;
    private String message;
    private MessageType type;

}
