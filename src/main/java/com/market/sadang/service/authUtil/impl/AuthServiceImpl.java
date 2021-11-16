package com.market.sadang.service.authUtil.impl;

import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.SignUp;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.repository.SignUpRepository;
import com.market.sadang.service.MemberService;
import com.market.sadang.service.authUtil.*;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final EmailService emailService;
    private final SignUpRepository signUpRepository;

    @Override
    public void verifyEmail(String key) throws NotFoundException {
        //key 받아서 사용자 찾음
//        String memberId = redisUtil.getData(key);
        SignUp signUp = signUpRepository.findByUuid(key);
        Member member = signUp.getMember();

        if (member == null)
            throw new NotFoundException("사용자가 조회되지 않습니다.");

        //해당 사용자 Role을 user로 변경
        modifyUserRole(member, UserRole.ROLE_USER);

        //redis에 해당 키 삭제
        signUpRepository.delete(signUp);
    }

    @Override
    public void modifyUserRole(Member member, UserRole userRole) {
        //Role 변경
        member.setRole(userRole);
        memberRepository.save(member);
    }

    @Override
    public Member findByUsername(String username) throws NotFoundException {
        Member member = memberRepository.findByUsername(username).get();
        if (member == null)
            return null;
        return member;
    }

    @Override
    public void sendVerificationMail(Member member) throws Exception {
        String VERIFICATION_LINK = "http://localhost:8080/verify/";
        if (member == null)
            throw new NotFoundException("사용자가 조회되지 않습니다.");

        //중복 없이 id 생성
        UUID uuid = UUID.randomUUID();

        signUpRepository.save(new SignUp(member,uuid.toString()));

        //메일 보냄
        emailService.sendMail(member.getEmail(), "SADANG 인증 메일입니다.", VERIFICATION_LINK + uuid.toString());
    }
}
