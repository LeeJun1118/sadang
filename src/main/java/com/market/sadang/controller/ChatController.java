package com.market.sadang.controller;

import com.google.gson.Gson;
import com.market.sadang.domain.ChatMessage;
import com.market.sadang.dto.chat.ChatMessageDto;
import com.market.sadang.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto dto) {
        if (ChatMessage.MessageType.ENTER.equals(dto.getTypes())){
            dto.setMessage(dto.getSender() + "님이 입장하셨습니다.");
        }
        ChatMessage chat = new ChatMessage(dto);

        // json으로 log 출력
        Gson gson = new Gson();
        String jsonString = gson.toJson(chat);
        log.info(jsonString);


        chatMessageRepository.save(chat);
        messagingTemplate.convertAndSend("/sub/chat/room/" + dto.getRoomId(), chat);
    }
}