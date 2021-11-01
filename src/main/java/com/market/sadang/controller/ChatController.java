package com.market.sadang.controller;

import com.google.gson.Gson;
import com.market.sadang.domain.ChatMessage;
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
    public void message(ChatMessage message/*, @Header("token") String token*/) {

        //토큰 유효성 검사
//        String memberId = jwtUtil.getUsername(token);
//        String username = memberRepository.findByUsername(memberId).getUsername();

        /*if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setSender("[알림]");
            message.setMessage(username + "님이 입장하셨습니다.");
        }*/

        // json으로 log 출력
        Gson gson = new Gson();
        System.out.println(gson.toJson(message));

        ChatMessage chatMessage = chatMessageRepository.save(new ChatMessage(message));

        try {
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), chatMessage);
        }
        catch (Exception ignored){}
    }
}