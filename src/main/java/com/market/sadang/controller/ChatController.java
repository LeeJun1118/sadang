package com.market.sadang.controller;

import com.google.gson.Gson;
import com.market.sadang.domain.ChatMessage;
import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.ReadStatus;
import com.market.sadang.dto.chat.ChatMessageDto;
import com.market.sadang.repository.ChatMessageRepository;
import com.market.sadang.repository.ChatRoomRepository;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.authUtil.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    //    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final SimpMessageSendingOperations messagingTemplate;


    // websocket "/pub/chat/message"로 들어오는 메시징을 처리
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto messageDto/*, @Header("roomId") String roomId,  @Header("username") String username*/) {

        //토큰 유효성 검사
//        String memberId = jwtUtil.getUsername(token);
//        String username = memberRepository.findByUsername(memberId).getUsername();

        /*if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setSender("[알림]");
            message.setMessage(username + "님이 입장하셨습니다.");
        }*/

        // json으로 log 출력
        Gson gson = new Gson();
        System.out.println("message=======" + gson.toJson(messageDto));

       /* if (Objects.equals(roomId, message.getRoomId()))
            message.setReceiverStatus(ReadStatus.Y);*/

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(messageDto.getRoomId());
        Member receiver = null;
        Member sender = null;


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

        //현재 사용자가 위치한 채팅방이 메세지가 온
      /*  if (Objects.equals(roomId, message.getRoomId())){
            message.setReceiverStatus(ReadStatus.Y);
        }*/


        ChatMessage chatMessage = chatMessageRepository.save(new ChatMessage(messageDto,sender,receiver));
        ChatMessageDto dto = new ChatMessageDto(chatMessage);

        try {

        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), dto);
        }
        catch (Exception ignored){}
    }
}