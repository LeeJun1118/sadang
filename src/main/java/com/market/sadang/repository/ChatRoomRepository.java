package com.market.sadang.repository;

import com.market.sadang.domain.ChatRoom;
import com.market.sadang.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByBoardIdAndSellerAndBuyer (Long boardId, Member seller, Member buyer);
    List<ChatRoom> findBySellerOrBuyer (Member seller, Member buyer);
    List<ChatRoom> findChatRoomBySellerOrBuyer (Member seller, Member buyer);
    List<ChatRoom> findAllBySellerOrBuyer (Member seller, Member buyer);
    ChatRoom findByBoardIdAndBuyer (Long id, Member buyer);
    ChatRoom findByRoomId (String roomId);
    int countChatRoomBySellerOrBuyer (Member seller, Member buyer);
}
