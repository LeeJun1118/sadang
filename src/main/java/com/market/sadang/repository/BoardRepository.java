package com.market.sadang.repository;

import com.market.sadang.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findByTitleContainingAndMember_Address(String search,String address);
}
