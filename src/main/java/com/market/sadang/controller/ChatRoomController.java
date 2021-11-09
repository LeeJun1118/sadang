package com.market.sadang.controller;

import com.google.gson.Gson;
import com.market.sadang.domain.*;
import com.market.sadang.dto.bord.BoardMemberDto;
import com.market.sadang.dto.bord.BoardUpdateRequestDto;
import com.market.sadang.dto.chat.ChatMessageListTimeDto;
import com.market.sadang.dto.chat.MessageListReadStatusDto;
import com.market.sadang.dto.member.MyBoardListResponseDto;
import com.market.sadang.dto.member.MyChatRoomListResponseDto;
import com.market.sadang.repository.BoardRepository;
import com.market.sadang.repository.ChatMessageRepository;
import com.market.sadang.repository.ChatRoomRepository;
import com.market.sadang.service.*;
import com.market.sadang.service.authUtil.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    private final ChatMessageService chatMessageService;
    private final BuyInterestedService buyInterestedService;

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
    public String roomDetail(Model model, HttpServletRequest request) throws Exception {

        Member username = null;
        if (cookieUtil.getCookie(request, "accessToken") != null) {
            username = memberService.searchMemberId(request);
        }

        //내가 들어간 방 목록 불러오기
        List<ChatRoom> roomList = chatRoomService.findRoomList(request);

        int lastId = 0;
        String lastMessageRoomId = null;
        // 각각의 roomId로 가장 마지막 메세지Id 불러와야함
        for (ChatRoom myRoom : roomList) {
            ChatMessage message = chatMessageRepository
                    .findFirstByRoomIdOrderByIdDesc(myRoom.getRoomId());
            if (message != null) {
                if (lastId < message.getId().intValue()) {
                    lastId = message.getId().intValue();
                }
            }
        }



        //내가 입장한 모든 방 각각의 메세지들 중 sender가 내가 아닌 메세지들의 readStatus가 N 인 메세지들의 수를 같이 반환
        List<MessageListReadStatusDto> roomListReadStatus = chatRoomService.findAllRoomReadStatus(roomList, username);


        model.addAttribute("roomList", roomListReadStatus);
        model.addAttribute("username", username.getUsername());

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

        // 글 작성자
        BoardMemberDto dto = boardService.findByIdMember(id);
        Member seller = dto.getMember();

        // 구매자
        Member buyer = memberService.searchMemberId(request);

        if (seller.getUsername() == buyer.getUsername()) {
            return "redirect:/chat/myChatRoom";
        }

        ChatRoom chatRoom = chatRoomRepository.findByBoardIdAndBuyer(id, buyer);

        if (chatRoom == null) {
            String roomId = UUID.randomUUID().toString();
            chatRoom = new ChatRoom(roomId, id, seller, buyer, dto.getTitle());
            chatRoomRepository.save(chatRoom);
        }
        //채팅한 내역 불러오기

        model.addAttribute("room", chatRoom);
        model.addAttribute("username", buyer.getUsername());

        return "redirect:/chat/room/enter/" + chatRoom.getRoomId();
    }


    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId, HttpServletRequest request) throws Exception {

        // 내가 입장해 있는 전체 채팅 방 목록
        List<ChatRoom> roomList = chatRoomService.findRoomList(request);

        for (ChatRoom room : roomList){
            System.out.println("roomDetail : " + room.getRoomId());
        }

        //사용자 이름
        Member username = memberService.searchMemberId(request);

        // 현재 입장한 채팅방
        ChatRoom thisRoom = chatRoomService.findByRoomId(roomId);

        String buy = buyInterestedService.findByBoardIdBuyStatus(thisRoom.getBoardId());


        // 채팅방에 입장 시 그 방의 모든 안읽은 메세지를 읽음으로 처리
        List<ChatMessage> messageList = chatMessageRepository.findAllByRoomIdAndReceiverAndReceiverStatus(roomId, username, ReadStatus.N);
        for (ChatMessage message : messageList) {
            if (Objects.equals(username, message.getReceiver()))
                chatMessageService.update(message.getId());
        }

        //내가 입장한 모든 방 각각의 메세지들 중 sender가 내가 아닌 메세지들의 readStatus가 N 인 메세지들의 수를 같이 반환
        List<MessageListReadStatusDto> roomListReadStatus = chatRoomService.findAllRoomReadStatus(roomList, username);
        List<ChatMessage> roomMessageList = chatMessageRepository.findAllByRoomId(thisRoom.getRoomId());
        List<ChatMessageListTimeDto> messages = new ArrayList<>();
        for (ChatMessage thisMessage : roomMessageList){
            messages.add(new ChatMessageListTimeDto(thisMessage));
        }

        for (MessageListReadStatusDto dto : roomListReadStatus){
            System.out.println("roomDetail 2 : " + dto.getRoomId());
        }

        model.addAttribute("roomList", roomListReadStatus);
        model.addAttribute("thisRoom", roomId);
        model.addAttribute("buy", buy);
        model.addAttribute("username", username.getUsername());
        model.addAttribute("messages", messages);
        return "/chat/chat";
    }

    // 채팅방 입장 화면
    @ResponseBody
    @PostMapping("/room/enter/{roomId}")
    public int unReadMessage(@PathVariable String roomId, HttpServletRequest request) throws Exception {

        //사용자 이름
        Member user = memberService.searchMemberId(request);

        List<ChatMessage> messageList = chatMessageRepository.findAllByRoomIdAndReceiverAndReceiverStatus(roomId, user, ReadStatus.N);
        for (ChatMessage message : messageList) {
            if (Objects.equals(user, message.getReceiver()))
                chatMessageService.update(message.getId());
        }
        return 0;
    }
}
