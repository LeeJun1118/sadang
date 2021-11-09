package com.market.sadang.service;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.BoardStatus;
import com.market.sadang.domain.BuyInterested;
import com.market.sadang.domain.Member;
import com.market.sadang.repository.BoardRepository;
import com.market.sadang.repository.BuyInterestedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BuyInterestedService {

    private final BuyInterestedRepository buyInterestedRepository;
    private final BoardRepository boardRepository;

    public BuyInterested findByMember(Member member) {
        return buyInterestedRepository.findByMember(member);
    }

    public List<BuyInterested> findByMemberAndBuyStatus(Member member, BoardStatus buy) {
        return buyInterestedRepository.findAllByMemberAndBuyStatus(member, buy);
    }

    public List<BuyInterested> findByMemberAndInterestedStatus(Member member, BoardStatus interested) {
        return buyInterestedRepository.findAllByMemberAndInterestedStatus(member, interested);
    }

    public String findByBoardIdBuyStatus(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        String status = null;
        try {
            status = "" + buyInterestedRepository.findByBoard(board).getBuyStatus();
            return status;
        } catch (Exception e) {
            return "none";
        }

    }

    public String findByBoardIdInterestedStatus(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        String status = null;
        try {
            status = "" + buyInterestedRepository.findByBoard(board).getInterestedStatus();
            return status;
        } catch (Exception e) {
            return "none";
        }
    }
}
