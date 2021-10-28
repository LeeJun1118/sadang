package com.market.sadang.domain;

import com.market.sadang.domain.dto.chat.ChatMessageDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private MessageType types;

    private String roomId;

    private String sender;

    private String message;


    public enum MessageType {
        ENTER, TALK, JOIN
    }

    @Builder
    public ChatMessage(ChatMessageDto dto) {
        this.types = dto.getTypes();
        this.roomId = dto.getRoomId();
        this.sender = dto.getSender();
        this.message = dto.getMessage();
    }
}
