package com.market.sadang.service.impl;

import com.market.sadang.domain.Member;
import com.market.sadang.domain.Salt;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.AuthService;
import com.market.sadang.service.SaltUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.plaf.metal.MetalMenuBarUI;

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

    @Override
    public Member loginUser(String id, String password) throws Exception {
        Member member = memberRepository.findByUsername(id);
        if(member == null)
            throw new Exception("사용자가 조회되지 않음");
        String salt = member.getSalt().getSalt();
        password = saltUtil.encodePassword(salt,password);
        if (!member.getPassword().equals(password))
            throw new Exception("비밀번호가 틀립니다.");

        return member;
    }
}
