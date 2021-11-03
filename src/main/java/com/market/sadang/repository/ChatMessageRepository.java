package com.market.sadang.repository;

import com.market.sadang.domain.ChatMessage;
import com.market.sadang.domain.ReadStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomId (String roomId/*, Sort sort*/);
    List<ChatMessage> findAllByRoomIdAndReceiverStatus (String roomId, ReadStatus readStatus);
    int countByRoomIdAndReceiverAndReceiverStatus (String roomId, String receiver, ReadStatus readStatus);
//    int countByRoomIdAndReceiverStatus (String roomId, ReadStatus readStatus);
    List<ChatMessage> countByRoomIdAndReceiverStatus (String roomId, ReadStatus readStatus);
//    List<ChatMessage> findAllByRoomIdAndReadStatus (String roomId, ReadStatus readStatus);
    List<ChatMessage> findAllByRoomIdAndReceiverAndReceiverStatus (String roomId, String receiver, ReadStatus readStatus);
    ChatMessage findFirstBySenderOrderByIdDesc (String sender);
    ChatMessage findFirstByRoomIdOrderByIdDesc (String roomId);
    int countAllByReceiverAndReceiverStatus (String receiver, ReadStatus readStatus);

}
