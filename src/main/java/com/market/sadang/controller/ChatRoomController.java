package com.market.sadang.controller;

import com.google.gson.Gson;
import com.market.sadang.domain.Board;
import com.market.sadang.domain.ChatMessage;
import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.Member;
import com.market.sadang.dto.bord.BoardUpdateRequestDto;
import com.market.sadang.dto.member.MyBoardListResponseDto;
import com.market.sadang.dto.member.MyChatRoomListResponseDto;
import com.market.sadang.repository.BoardRepository;
import com.market.sadang.repository.ChatMessageRepository;
import com.market.sadang.repository.ChatRoomRepository;
import com.market.sadang.service.BoardService;
import com.market.sadang.service.ChatRoomService;
import com.market.sadang.service.MemberService;
import com.market.sadang.service.authUtil.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    //*
    // 채팅 리스트 화면
   /* @GetMapping("/room/newChatRoom")
    public String rooms(Model model, HttpServletRequest request) {

        //

        ChatRoom room = chatRoomRepository.findByRoomId("607fe131-bb73-4b08-9842-7f51e7130461");

        List<ChatMessage> messages = chatMessageRepository
                .findAllByRoomId("607fe131-bb73-4b08-9842-7f51e7130461", Sort.by(Sort.Direction.DESC,"id"));

        String username = null;
        if ( cookieUtil.getCookie(request, "accessToken") != null) {
            username = memberService.searchMemberId(request).getUsername();
        }

        List<ChatRoom> roomList = chatRoomService.findRoomList(request);
        model.addAttribute("roomList", roomList);

        model.addAttribute("room", room);
        model.addAttribute("username", username);
        model.addAttribute("messages", messages);
        // roomList도 보냄
        //message List 보낼때  sender, receiver, 현재 로그인한 username 같이 보냄

        // 프론트에서 messageList.sender 가 현재 username이랑 같다면


        return "/chat/chat";
    }*/

    @GetMapping("/myChatRoom")
    public String roomDetail(Model model, HttpServletRequest request) {

        String username = null;
        if ( cookieUtil.getCookie(request, "accessToken") != null) {
            username = memberService.searchMemberId(request).getUsername();
        }

        //내가 들어간 방 목록 불러오기
        List<ChatRoom> roomList = chatRoomService.findRoomList(request);

        int lastId = 0;
        String lastMessageRoomId = null;
        // 각각의 roomId로 가장 마지막 메세지Id 불러와야함
        for (ChatRoom myRoom : roomList){
            ChatMessage message = chatMessageRepository
                    .findFirstByRoomIdOrderByIdDesc(myRoom.getRoomId());
            if (lastId < message.getId()){
                lastId = message.getId().intValue();
                lastMessageRoomId = message.getRoomId();
            }
        }

//        ChatRoom thisRoom = chatRoomRepository.findByRoomId(lastMessageRoomId);
        ChatRoom thisRoom =chatRoomService.findByRoomId(lastMessageRoomId);

        List<ChatMessage> messages = chatMessageRepository
                .findAllByRoomId(lastMessageRoomId/*, Sort.by(Sort.Direction.DESC,"id")*/);



        model.addAttribute("roomList", roomList);

        model.addAttribute("thisRoom", thisRoom);
        model.addAttribute("username", username);
        model.addAttribute("messages", messages);
//        model.addAttribute("token", token);
        return "/chat/chat";
    }


    // 모든 채팅방 목록 반환
    /*@GetMapping("/rooms")
    @ResponseBody
    public ModelAndView room(ModelAndView modelAndView) {
        List<ChatRoom> rooms = chatRoomRepository.findAll();

        modelAndView.addObject("rooms", rooms);
        modelAndView.setViewName("chat/rooms");

        return modelAndView;
    }*/

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
//        String token = cookieUtil.getCookie(request, "accessToken").getValue();

        if (chatRoom == null) {
            String roomId = UUID.randomUUID().toString();
            chatRoom = new ChatRoom(roomId, id, sellerName, buyerName);
            chatRoomRepository.save(chatRoom);
        }
        //채팅한 내역 불러오기

        model.addAttribute("room", chatRoom);
        model.addAttribute("username", buyerName);
//        model.addAttribute("token", token);

        return "redirect:/chat/room/enter/" + chatRoom.getRoomId();
    }


    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId, HttpServletRequest request) {

        ChatRoom thisRoom =chatRoomService.findByRoomId(roomId);
        List<ChatMessage> messages = chatMessageRepository
                .findAllByRoomId(roomId);

        String username = null;
        if ( cookieUtil.getCookie(request, "accessToken") != null) {
            username = memberService.searchMemberId(request).getUsername();
        }

        List<ChatRoom> roomList = chatRoomService.findRoomList(request);
        model.addAttribute("roomList", roomList);

        model.addAttribute("thisRoom", thisRoom);
        model.addAttribute("username", username);
        model.addAttribute("messages", messages);
//        model.addAttribute("token", token);
        return "/chat/chat";
    }

    // 특정 채팅방 조회
    /*@GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }*/

    /*@GetMapping("/myChatRoom")
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

        List<ChatRoom> roomList = chatRoomService.findRoomList(request);
        modelAndView.addObject("roomIdList", roomList);

        modelAndView.addObject("myChatRoomList", dtoList);
        modelAndView.setViewName("/member/myChatRoom");

        return modelAndView;
    }*/
}
