package com.market.sadang.service;

import com.market.sadang.domain.*;
import com.market.sadang.domain.enumType.EnterStatus;
import com.market.sadang.dto.chat.MessageListReadStatusDto;
import com.market.sadang.repository.ChatMessageRepository;
import com.market.sadang.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;


    public List<ChatRoom> findRoomList() {

        try {
            Member member = memberService.findByMemberRequest();
            System.out.println("ChatRoomService findRoomList getUsername : " + member.getUsername());
            List<ChatRoom> roomList = chatRoomRepository.findAllBySellerOrBuyer(member, member);
            List<ChatRoom> myRooms = new ArrayList<>();
            for (ChatRoom room : roomList) {
                if (Objects.equals(room.getSeller().getId(), member.getId()) && room.getSellerStatus() == EnterStatus.Y) {
                    myRooms.add(room);
                } else if (Objects.equals(room.getBuyer().getId(), member.getId()) && room.getBuyerStatus() == EnterStatus.Y) {
                    myRooms.add(room);
                }
            }

            return myRooms;

        } catch (Exception e) {
            System.out.println("ChatRoomService findRoomList : " + e.getMessage());
            return null;
        }
    }

    public ChatRoom findByRoomId(String roomId) {
        try {
            ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
            return chatRoom;
        } catch (Exception e) {
            return null;
        }
    }

   /* public List<MessageListReadStatusDto> findRoomListReadStatus(HttpServletRequest request) {
        List<ChatRoom> roomList = null;
        List<MessageListReadStatusDto> listDto = new ArrayList<>();

        Member member = memberService.findByMemberRequest();
        roomList = chatRoomRepository.findBySellerNameOrBuyerName(member.getUsername(), member.getUsername());

        // 내가 입장해있는 채팅방들에서 sender가 내가 아닌 메세지의 readStatus 가 N인 메세지의 수를 셈
        for (ChatRoom room : roomList){
            int countReadStatusN = chatMessageRepository.countByRoomIAndReadStatus(room.getRoomId(), ReadStatus.N);
            MessageListReadStatusDto dto = new MessageListReadStatusDto(room,countReadStatusN);
            listDto.add(dto);
        }

        return listDto;
    }*/

    public List<MessageListReadStatusDto> findAllRoomReadStatus(List<ChatRoom> roomList, Member user) {
        List<MessageListReadStatusDto> listDto = new ArrayList<>();
        MessageListReadStatusDto dto = null;
        String createdDateTime = null;
        /*List<ChatMessage> messageList = new ArrayList<>();
        int sender = 0;
        int receiver = 0;*/
        // 내가 입장해있는 채팅방들에서 sender가 내가 아닌 메세지의 readStatus 가 N인 메세지의 수를 셈
        for (ChatRoom room : roomList) {
            int countReadStatusN = chatMessageRepository.countByRoomIdAndReceiverAndReceiverStatus(room.getRoomId(), user, ReadStatus.N);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
            System.out.println("ChatRoomService findAllRoomReadStatus roomId : " + room.getRoomId());
            System.out.println("ChatRoomService findFirstByRoomIdOrderByIdDesc : " + chatMessageRepository.findFirstByRoomIdOrderByIdDesc(room.getRoomId()));
            try {
                createdDateTime = chatMessageRepository.findFirstByRoomIdOrderByIdDesc(room.getRoomId()).getCreatedDate().format(formatter);
            } catch (Exception e) {
                createdDateTime = null;
                System.out.println("ChatRoomService findAllRoomReadStatus error : " + e.getMessage());
            }


            dto = new MessageListReadStatusDto(room, countReadStatusN, createdDateTime);
            listDto.add(dto);
        }

        return listDto;
    }

    @Transactional
    public void delete(String roomId) {

        Member user = memberService.findByMemberRequest();
        ChatRoom chatRoom = findByRoomId(roomId);

        try {
            String buyer = chatRoom.getBuyer().getUsername();
            String seller = chatRoom.getSeller().getUsername();

            if (Objects.equals(buyer, user.getUsername())) {
                chatRoom.updateBuyerEnterStatus(EnterStatus.N);
            } else
                chatRoom.updateSellerEnterStatus(EnterStatus.N);

        } catch (Exception e) {
            chatRoomRepository.delete(chatRoom);
        }


    }
}
