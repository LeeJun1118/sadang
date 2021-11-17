package com.market.sadang.controller;

import com.google.gson.Gson;
import com.market.sadang.domain.ChatMessage;
import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.enumType.EnterStatus;
import com.market.sadang.domain.Member;
import com.market.sadang.dto.chat.ChatMessageDto;
import com.market.sadang.repository.ChatMessageRepository;
import com.market.sadang.repository.ChatRoomRepository;
import com.market.sadang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final SimpMessageSendingOperations messagingTemplate;


    // websocket "/pub/chat/message"로 들어오는 메시징을 처리
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto messageDto) {


        // json으로 log 출력
        Gson gson = new Gson();
        System.out.println("message=======" + gson.toJson(messageDto));


        ChatRoom chatRoom = chatRoomRepository.findByRoomId(messageDto.getRoomId());
        Member receiver;
        Member sender;

        // 채팅방에서 둘 중 한명이 나갔다면 다시 입장
        if (chatRoom.getSellerStatus() == EnterStatus.N){
            chatRoom.setSellerStatus(EnterStatus.Y);
            chatRoom.setSellerEnterDate(LocalDateTime.now());
            chatRoomRepository.save(chatRoom);
        }
        else if(chatRoom.getBuyerStatus() == EnterStatus.N){
            chatRoom.setBuyerStatus(EnterStatus.Y);
            chatRoom.setBuyerEnterDate(LocalDateTime.now());
            chatRoomRepository.save(chatRoom);
        }



        // ChatRoom 에는 Buyer, Seller 둘이 있는데
        // 둘 중 Sender 가 아닌 사람이 receiver 가 된다.
        if (Objects.equals(messageDto.getSender(), chatRoom.getBuyer().getUsername())){
            receiver = chatRoom.getSeller();
            sender =  chatRoom.getBuyer();
        }
        else{
            receiver = chatRoom.getBuyer();
            sender = chatRoom.getSeller();
        }
        messageDto.setReceiver(receiver.getUsername());

        ChatMessage chatMessage = chatMessageRepository.save(new ChatMessage(messageDto,chatRoom,sender,receiver));
        ChatMessageDto dto = new ChatMessageDto(chatMessage);

        try {

        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), dto);
        }
        catch (Exception ignored){}
    }
}