package com.market.sadang.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ChatMessage extends BaseTimeEntity{

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지


    @Builder
    public ChatMessage(ChatMessage entity) {
        this.type = entity.getType();
        this.roomId = entity.getRoomId();
        this.sender = entity.getSender();
        this.message = entity.getMessage();
    }
}
