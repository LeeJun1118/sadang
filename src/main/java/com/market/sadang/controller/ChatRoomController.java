package com.market.sadang.controller;

import com.google.gson.Gson;
import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.Member;
import com.market.sadang.repository.ChatRoomRepository;
import com.market.sadang.service.MemberService;
import com.market.sadang.service.authUtil.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final CookieUtil cookieUtil;

    //*
    // 채팅 리스트 화면
   /* @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }*/


    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public ModelAndView room(ModelAndView modelAndView) {
        List<ChatRoom> rooms = chatRoomRepository.findAllRoom();

        modelAndView.addObject("rooms", rooms);
        modelAndView.setViewName("chat/rooms");

        return modelAndView;
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ChatRoom createRoom(@RequestParam String roomName) {
        return chatRoomRepository.createChatRoom(roomName);
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId, HttpServletRequest request) {
        ChatRoom room = chatRoomRepository.findRoomById(roomId);

        String token = cookieUtil.getCookie(request, "accessToken").getValue();
        String username = memberService.searchMemberId(request).getUsername();

        model.addAttribute("room", room);
        model.addAttribute("username", username);
        model.addAttribute("token", token);
        return "/chat/room";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}
