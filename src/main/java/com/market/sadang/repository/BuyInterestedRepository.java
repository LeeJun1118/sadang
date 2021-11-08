package com.market.sadang.repository;

import com.market.sadang.domain.BuyInterested;
import com.market.sadang.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyInterestedRepository extends JpaRepository<BuyInterested, Long> {
    BuyInterested findByMember (Member member);
}
