package com.market.sadang.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.sadang.domain.ChatMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class ChatRoomService {
    private Set<WebSocketSession> sessions = new HashSet<>();


    public void handleMessage(WebSocketSession session, ChatMessage chatMessage,
                              ObjectMapper objectMapper) throws IOException {
        if (chatMessage.getTypes() == ChatMessage.MessageType.ENTER) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
        } /*else if (chatMessage.getTypes() == ChatMessage.MessageType.) {
            sessions.remove(session);
            chatMessage.setMessage(chatMessage.getSender() + "님임 퇴장하셨습니다.");
        } */else {
            chatMessage.setMessage(chatMessage.getSender() + " : " + chatMessage.getMessage());
        }
        send(chatMessage, objectMapper);
    }

    private void send(ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.
                writeValueAsString(chatMessage.getMessage()));
        for (WebSocketSession sess : sessions) {
            sess.sendMessage(textMessage);
        }
    }
}
