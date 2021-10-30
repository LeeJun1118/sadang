package com.market.sadang.controller;

import com.google.gson.Gson;
import com.market.sadang.domain.Board;
import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.Member;
import com.market.sadang.dto.bord.BoardUpdateRequestDto;
import com.market.sadang.dto.member.MyBoardListResponseDto;
import com.market.sadang.dto.member.MyChatRoomListResponseDto;
import com.market.sadang.repository.BoardRepository;
import com.market.sadang.repository.ChatRoomRepository;
import com.market.sadang.service.BoardService;
import com.market.sadang.service.MemberService;
import com.market.sadang.service.authUtil.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final CookieUtil cookieUtil;
    private final BoardService boardService;

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
        List<ChatRoom> rooms = chatRoomRepository.findAll();

        modelAndView.addObject("rooms", rooms);
        modelAndView.setViewName("chat/rooms");

        return modelAndView;
    }

    // 채팅방 생성
    // 게시글에서 채팅 클릭 -> BoardId, UserId 받아옴
    // chatRoomRepo 에 BoardId, Board writer username, username, chatRoomId 로 생성
    // PathVariable 로 방 받고, HttpRequest 로 사용자 받아옴
    // 동일한 내용의 chatRoom이 있으면 채팅방으로 이동
    // 없으면 생성 후 이동
    @GetMapping("/room/{id}")
    public String createRoom(@PathVariable Long id,
                             HttpServletRequest request,
                             Model model) {

        String sellerName = boardService.findByIdMember(id).getMember();
        String buyerName = memberService.searchMemberId(request).getUsername();

        if (sellerName == buyerName){
            return "redirect:/chat/myChatRoom";
        }

        ChatRoom chatRoom = chatRoomRepository.findByBoardIdAndBuyerName(id,buyerName);
        String token = cookieUtil.getCookie(request, "accessToken").getValue();

        if (chatRoom == null) {
            String roomId = UUID.randomUUID().toString();
            chatRoom = new ChatRoom(roomId, id, sellerName, buyerName);
            chatRoomRepository.save(chatRoom);
        }


        model.addAttribute("room", chatRoom);
        model.addAttribute("username", buyerName);
        model.addAttribute("token", token);

        return "/chat/room";
    }

    /*@GetMapping("/room/enter/{id}")
    public String enterRoom(@PathVariable Long id,
                             HttpServletRequest request,
                             Model model) {

        String sellerName = boardService.findByIdMember(id).getMember();

        String buyerName = memberService.searchMemberId(request).getUsername();
        String token = cookieUtil.getCookie(request, "accessToken").getValue();

        //
        ChatRoom chatRoom = chatRoomRepository.findByBoardIdAndSellerNameAndBuyerName(id, sellerName, buyerName);
//        System.out.println("ChatRoom Controller : enter room's chatRoom roomId == " + chatRoom.getRoomId());
//        System.out.println("ChatRoom Controller : enter room's sellerName == " + sellerName);
//        System.out.println("ChatRoom Controller : enter room's buyerName == " + buyerName);


        if (chatRoom == null) {
            String roomId = UUID.randomUUID().toString();
            chatRoom = new ChatRoom(roomId, id, sellerName, buyerName);
            chatRoomRepository.save(chatRoom);
        }

        model.addAttribute("room", chatRoom);
        model.addAttribute("buyerName", buyerName);
        model.addAttribute("sellerName", sellerName);
        model.addAttribute("token", token);

        return "/chat/room";
    }*/

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId, HttpServletRequest request) {
        ChatRoom room = chatRoomRepository.findByRoomId(roomId);

        String token = cookieUtil.getCookie(request, "accessToken").getValue();
        String username = memberService.searchMemberId(request).getUsername();

        model.addAttribute("room", room);
        model.addAttribute("username", username);
        model.addAttribute("token", token);
        return "/chat/room";
    }

    // 특정 채팅방 조회
    /*@GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }*/

    @GetMapping("/myChatRoom")
    public ModelAndView myChatRoom(HttpServletRequest request,
                                   ModelAndView modelAndView) {
        Member member = memberService.searchMemberId(request);
        List<ChatRoom> chatRoomList = chatRoomRepository
                .findBySellerNameOrBuyerName(member.getUsername(), member.getUsername());

        List<MyChatRoomListResponseDto> dtoList = new ArrayList<>();

        if (chatRoomList != null) {
            for (ChatRoom chatRoom : chatRoomList) {
                dtoList.add(new MyChatRoomListResponseDto(chatRoom));
            }
        }

        modelAndView.addObject("myChatRoomList", dtoList);
        modelAndView.setViewName("/member/myChatRoom");

        return modelAndView;
    }
}
