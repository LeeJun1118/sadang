package com.market.sadang.service;

import com.market.sadang.domain.Member;
import com.market.sadang.domain.SecurityMember;
import com.market.sadang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);
        if (member == null){
            throw new UsernameNotFoundException(username + " : 사용자가 존재하지 않음");
        }
        return new SecurityMember(member);
    }
}
