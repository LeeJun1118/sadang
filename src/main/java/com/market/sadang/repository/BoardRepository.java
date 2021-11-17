package com.market.sadang.repository;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.enumType.BoardStatus;
import com.market.sadang.domain.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
//    List<Board> findAllByTitleContainingOrAddressContaining (String search, String address);
    List<Board> findAllByTitleContainingOrAddressContainingAndSellStatus (String search, String address, BoardStatus status);

//    List<Board> findAllByMember(Member member);
    List<Board> findAllByMemberAndSellStatus(Member member, BoardStatus status);

//    List<Board> findAllByStatus (BoardStatus status);
    List<Board> findAllBySellStatus (Sort sort, BoardStatus status);
}
