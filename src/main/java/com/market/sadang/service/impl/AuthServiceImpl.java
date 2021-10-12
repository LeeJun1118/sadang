package com.market.sadang.service.impl;

import com.market.sadang.domain.Member;
import com.market.sadang.domain.Salt;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.AuthService;
import com.market.sadang.service.SaltUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final SaltUtil saltUtil;

    @Override
    public void signUpUser(Member member) {
        String password = member.getPassword();
        String salt = saltUtil.genSalt();
        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt,password));
        memberRepository.save(member);
    }
}
