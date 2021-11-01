package com.market.sadang.service;

import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.Member;
import com.market.sadang.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;


    public List<ChatRoom> findRoomList(HttpServletRequest request) {
        List<ChatRoom> roomList = null;
        try {
            Member member = memberService.searchMemberId(request);
            roomList = chatRoomRepository.findBySellerNameOrBuyerName(member.getUsername(), member.getUsername());

        } catch (Exception e) {
            return null;
        }

        return roomList;
    }

    public ChatRoom findByRoomId(String roomId) {
        try {
            ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
            return chatRoom;
        } catch (Exception e) {
            return null;
        }
    }
}
