package com.market.sadang.service.authUtil.impl;

import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.Salt;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.authUtil.*;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final SaltUtil saltUtil;
    private final RedisUtil redisUtil;
    private final EmailService emailService;

    @Override
    @Transactional //begin,commit 자동수행, 예외 발생 시 rollback 자동 수행
    public Member signUpUser(Member member) {
        //비밀 번호 암호화 후 사용자 저장

        String password = member.getPassword();
        String salt = saltUtil.genSalt();
        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt,password));
        return memberRepository.save(member);
    }

    @Override
    @Transactional //begin,commit 자동수행, 예외 발생 시 rollback 자동 수행
    public Member loginUser(String userId, String password) throws Exception {
        //id 로 찾아서
        Member member = memberRepository.findByUsername(userId);
        if(member == null)
            throw new Exception("사용자가 조회되지 않음");
        String salt = member.getSalt().getSalt();
        password = saltUtil.encodePassword(salt,password);
        if (!member.getPassword().equals(password))
            throw new Exception("비밀번호가 틀립니다.");

        return member;
    }






    @Override
    public void verifyEmail(String key) throws NotFoundException {
        //key 받아서 사용자 찾음
        String memberId = redisUtil.getData(key);
        Member member = memberRepository.findByUsername(memberId);

        if (member == null)
            throw new NotFoundException("사용자가 조회되지 않습니다.");

        //해당 사용자 Role을 user로 변경
        modifyUserRole(member, UserRole.ROLE_USER);

        //redis에 해당 키 삭제
        redisUtil.deleteData(key);
    }







    /*@Override
    public void verifyEmail(String key) throws NotFoundException {
        //key 받아서 사용자 찾음
        String memberId = redisUtil.getData(key);
        Member member = memberRepository.findByUserId(memberId);

        if (member == null)
            throw new NotFoundException("사용자가 조회되지 않습니다.");

        //해당 사용자 Role을 user로 변경
        modifyUserRole(member, UserRole.ROLE_USER);

        //redis에 해당 키 삭제
        redisUtil.deleteData(key);
    }*/

    @Override
    public void modifyUserRole(Member member, UserRole userRole) {
        //Role 변경
        member.setRole(userRole);
        memberRepository.save(member);
    }

    @Override
    public Member findByUserId(String userId) throws NotFoundException {
        Member member = memberRepository.findByUsername(userId);
        if (member == null)
            return null;
        return member;
    }

    @Override
    public void sendVerificationMail(Member member) throws Exception {
        String VERIFICATION_LINK = "http://localhost:8080/verify/";
        if (member == null)
            throw new NotFoundException("사용자가 조회되지 않습니다.");

        String verify = ""+ member.getId();
        SecretKey key = AESCryptoUtil.getKey();




        //중복 없이 id 생성
        UUID uuid = UUID.randomUUID();

        //uuid 만료시간
        redisUtil.setDataExpire(uuid.toString(), member.getUsername(), 60 * 30L);

        //메일 보냄
        emailService.sendMail(member.getEmail(), "SADANG 인증 메일입니다.",VERIFICATION_LINK + uuid.toString());


//        emailService.sendMail(member.getEmail(), "SADANG 인증 메일입니다.",VERIFICATION_LINK + uuid.toString());

    }

    /*@Override
    public void sendVerificationMail(Member member) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/verify/";
        if (member == null)
            throw new NotFoundException("사용자가 조회되지 않습니다.");

        //중복 없이 id 생성
        UUID uuid = UUID.randomUUID();

        //uuid 만료시간
        redisUtil.setDataExpire(uuid.toString(), member, 60 * 30L);
        System.out.println("===========-->"+redisUtil.getData(uuid.toString()));

        //메일 보냄
        emailService.sendMail(member.getEmail(), "SADANG 인증 메일입니다.",VERIFICATION_LINK + uuid.toString());

    }*/

}
