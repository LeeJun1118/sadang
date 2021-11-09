package com.market.sadang.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.market.sadang.dto.chat.ChatMessageDto;
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

    @Column
    private MessageType type; // 메시지 타입

    @ManyToOne
    private ChatRoom chatRoom;

    @Column
    private String roomId; // 방번호

    @ManyToOne
    private Member sender; // 메시지 보낸사람

    @ManyToOne
    private Member receiver; // 메시지 받는 사람

    @Column
    private String message; // 메시지

    @Enumerated(EnumType.STRING)
    private ReadStatus senderStatus;

    @Enumerated(EnumType.STRING)
    private ReadStatus receiverStatus;

    @Builder
    public ChatMessage(ChatMessageDto dto,ChatRoom chatRoom,Member sender, Member receiver) {
        this.type = dto.getType();
        this.chatRoom = chatRoom;
        this.roomId = dto.getRoomId();
        this.sender = sender;
        this.receiver = receiver;
        this.message = dto.getMessage();
        this.senderStatus = dto.getSenderStatus();
        this.receiverStatus = dto.getReceiverStatus();
    }

    public void update () {
        this.receiverStatus = ReadStatus.Y;
    }
}
