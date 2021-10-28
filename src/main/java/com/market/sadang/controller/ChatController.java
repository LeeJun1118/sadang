package com.market.sadang.controller;

import com.market.sadang.domain.dto.chat.ChatMessage;
import com.market.sadang.domain.dto.chat.ChatRoom;
import com.market.sadang.domain.dto.form.ChatRoomForm;
import com.market.sadang.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

 /*   private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType()))
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }*/

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/rooms")
    public String rooms(Model model){
        model.addAttribute("rooms",chatRoomRepository.findAllRoom());
        return "chat/rooms";
    }

    @GetMapping("/rooms/{id}")
    public String room(@PathVariable String id, Model model){
        ChatRoom room = chatRoomRepository.findRoomById(id);
        model.addAttribute("room",room);
        return "chat/room";
    }

    @GetMapping("/new")
    public String create(Model model){
        ChatRoomForm form = new ChatRoomForm();
        model.addAttribute("form",form);
        return "chat/newRoom";
    }

    @PostMapping("/room/new")
    public String createRoom(ChatRoomForm form){
        chatRoomRepository.createChatRoom(form.getName());
        return "redirect:/rooms";
    }

}