package com.market.sadang.repository;

import com.market.sadang.domain.ChatMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomId (String roomId/*, Sort sort*/);
    ChatMessage findFirstBySenderOrderByIdDesc (String sender);
    ChatMessage findFirstByRoomIdOrderByIdDesc (String roomId);

}
