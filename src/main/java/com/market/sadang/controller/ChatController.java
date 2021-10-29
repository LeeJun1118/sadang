package com.market.sadang.controller;

import com.google.gson.Gson;
import com.market.sadang.domain.ChatMessage;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;


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


    // websocket "/pub/chat/message"로 들어오는 메시징을 처리
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {

        System.out.println("Chat Controller header token == " + token);
        String memberId = jwtUtil.getUsername(token);
        String username = memberRepository.findByUsername(memberId).getUsername();

        System.out.println("Chat Controller header username == " + username);

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
//            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setSender("[알림]");
            message.setMessage(username + "님이 입장하셨습니다.");
        }

        // json으로 log 출력
        Gson gson = new Gson();
        String jsonString = gson.toJson(message);
        System.out.println("ChatController Chat Message's message == " + jsonString);

        // Websocket에 발행된 메시지를 redis로 발행(publish)
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);

    }
}