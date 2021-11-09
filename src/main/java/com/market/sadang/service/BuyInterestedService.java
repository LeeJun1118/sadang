package com.market.sadang.service;

import com.market.sadang.domain.BoardStatus;
import com.market.sadang.domain.BuyInterested;
import com.market.sadang.domain.Member;
import com.market.sadang.repository.BuyInterestedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BuyInterestedService {

    private final BuyInterestedRepository buyInterestedRepository;

    public BuyInterested findByMember(Member member) {
        return buyInterestedRepository.findByMember(member);
    }

    public List<BuyInterested> findByMemberAndBuyStatus(Member member, BoardStatus buy) {
        return buyInterestedRepository.findAllByMemberAndBuyStatus(member, buy);
    }

    public List<BuyInterested> findByMemberAndInterestedStatus(Member member, BoardStatus interested) {
        return buyInterestedRepository.findAllByMemberAndInterestedStatus(member, interested);
    }
}
