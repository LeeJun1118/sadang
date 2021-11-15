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
    private final MemberService memberService;

    public BuyInterested findByMember(Member member) {
        return buyInterestedRepository.findByMember(member);
    }

    public List<BuyInterested> findByMemberAndBuyStatusOrInterestedStatus(Member member, BoardStatus status) {
        if (status == BoardStatus.buy)
        return buyInterestedRepository.findAllByMemberAndBuyStatus(member, status);
        else
            return buyInterestedRepository.findAllByMemberAndInterestedStatus(member, status);
    }

    public String findByBoardIdBuyStatus(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Member member = memberService.findByMemberRequest();

        String status = null;
        try {
            status = "" + buyInterestedRepository.findByBoardAndMember(board,member).getBuyStatus();
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
