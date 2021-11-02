package com.market.sadang.service;

import com.market.sadang.domain.ChatMessage;
import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.ReadStatus;
import com.market.sadang.dto.chat.MessageListReadStatusDto;
import com.market.sadang.repository.ChatMessageRepository;
import com.market.sadang.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;


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

   /* public List<MessageListReadStatusDto> findRoomListReadStatus(HttpServletRequest request) {
        List<ChatRoom> roomList = null;
        List<MessageListReadStatusDto> listDto = new ArrayList<>();

        Member member = memberService.searchMemberId(request);
        roomList = chatRoomRepository.findBySellerNameOrBuyerName(member.getUsername(), member.getUsername());

        // 내가 입장해있는 채팅방들에서 sender가 내가 아닌 메세지의 readStatus 가 N인 메세지의 수를 셈
        for (ChatRoom room : roomList){
            int countReadStatusN = chatMessageRepository.countByRoomIAndReadStatus(room.getRoomId(), ReadStatus.N);
            MessageListReadStatusDto dto = new MessageListReadStatusDto(room,countReadStatusN);
            listDto.add(dto);
        }

        return listDto;
    }*/

    public List<MessageListReadStatusDto> findAllRoomReadStatus(List<ChatRoom> roomList, String username) {
        List<MessageListReadStatusDto> listDto = new ArrayList<>();
        MessageListReadStatusDto dto = null;
        /*List<ChatMessage> messageList = new ArrayList<>();
        int sender = 0;
        int receiver = 0;*/
        // 내가 입장해있는 채팅방들에서 sender가 내가 아닌 메세지의 readStatus 가 N인 메세지의 수를 셈
        for (ChatRoom room : roomList) {
            int countReadStatusN = chatMessageRepository.countByRoomIdAndReceiverAndReceiverStatus(room.getRoomId(), username, ReadStatus.N);
            /*try {

                messageList = chatMessageRepository.countByRoomIdAndReceiverStatus(room.getRoomId(), ReadStatus.N);
//            dto = new MessageListReadStatusDto(room, messageList.size());

                for (ChatMessage message : messageList) {
                    if (message.getSender() == username) {
                        receiver++;
                    } else
                        sender++;
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            dto = new MessageListReadStatusDto(room, sender, receiver);
*/
            dto = new MessageListReadStatusDto(room, countReadStatusN);
            listDto.add(dto);
        }

        return listDto;
    }
}
