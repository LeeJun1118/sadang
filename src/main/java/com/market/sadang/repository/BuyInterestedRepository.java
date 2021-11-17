package com.market.sadang.repository;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.enumType.BoardStatus;
import com.market.sadang.domain.BuyInterested;
import com.market.sadang.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyInterestedRepository extends JpaRepository<BuyInterested, Long> {
    BuyInterested findByMember (Member member);

    List<BuyInterested> findAllByMemberAndBuyStatus(Member member, BoardStatus buy);

    List<BuyInterested> findAllByMemberAndInterestedStatus(Member member, BoardStatus interested);

    BuyInterested findByBoard(Board board);

    BuyInterested findByBoardAndMember (Board board, Member member);
}
