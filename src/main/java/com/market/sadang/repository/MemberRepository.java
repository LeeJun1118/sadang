package com.market.sadang.repository;

import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    //    Member findByUsername(String username);
    Optional<Member> findByUsername(String username);

    Integer countByUsername(String username);

    List<Member> findAllByRole (UserRole role);
}
