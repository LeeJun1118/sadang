package com.market.sadang.repository;

import com.market.sadang.domain.ChatMessage;
import com.market.sadang.domain.ReadStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomId (String roomId/*, Sort sort*/);
//    List<ChatMessage> findAllByRoomIdAndAndReadStatus (String roomId, ReadStatus readStatus);
    int countByRoomIdAndReadStatus (String roomId, ReadStatus readStatus);
    List<ChatMessage> findAllByRoomIdAndReadStatus (String roomId, ReadStatus readStatus);
    ChatMessage findFirstBySenderOrderByIdDesc (String sender);
    ChatMessage findFirstByRoomIdOrderByIdDesc (String roomId);

}
