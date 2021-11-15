package com.market.sadang.service;

import com.market.sadang.domain.ChatMessage;
import com.market.sadang.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    //DB에 쿼리를 날리는 부분이 없다
    //JPA 영속성 컨텍스트 : Entity를 영구 저장하는 환경
    //JPA Entity Manager 가 활성화된 상태로 트랜잭션 내에서 DB data 를 가져옴
    // ---> 영속성 컨텍스트 유지 상태
    // 영속성 컨텍스트가 유지된 상태에서 해당 data 값을 변경할 경우, 트랜잭션이 종료되는
    // 시점에 해당 테이블에 변경분을 반영함
    // ==> Entity 객체의 값만 변경하면 별도로 update 쿼리를 날릴 필요가 없음
    @Transactional
    public void update(Long id) throws Exception {
        ChatMessage message = chatMessageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메세지가 존재하지 않습니다."));

        message.update();
    }


}
