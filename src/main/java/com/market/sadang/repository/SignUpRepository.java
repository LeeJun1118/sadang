package com.market.sadang.repository;

import com.market.sadang.domain.Member;
import com.market.sadang.domain.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpRepository extends JpaRepository<SignUp, Long> {
    SignUp findByUuid (String uuid);
    SignUp findByMember (Member member);
}
