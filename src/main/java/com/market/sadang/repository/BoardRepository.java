package com.market.sadang.repository;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByTitleContainingOrAddressContaining (String search, String address);
    List<Board> findAllByMember(Member member);
}
