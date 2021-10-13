package com.market.sadang.service.impl;

import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.Salt;
import com.market.sadang.domain.SignUpForm;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.AuthService;
import com.market.sadang.service.EmailService;
import com.market.sadang.service.RedisUtil;
import com.market.sadang.service.SaltUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.plaf.metal.MetalMenuBarUI;
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
    @Transactional
    public void signUpUser(Member member) {
        /*Member member = new Member();
        member.setUsername(signUpForm.getUsername());

        member.setAddress(signUpForm.getAddress());
//        member.setEmail(signUpForm.getEmail());
*/
//        String password = signUpForm.getPassword();
        String password = member.getPassword();
        String salt = saltUtil.genSalt();
        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt,password));
        memberRepository.save(member);
    }

    @Override
    @Transactional
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

    @Override
    public void verifyEmail(String key) throws NotFoundException {
        String memberId = redisUtil.getData(key);
        Member member = memberRepository.findByUsername(memberId);
        System.out.println(member.getUsername());
        System.out.println(member.getEmail());
        if (member == null)
            throw new NotFoundException("사용자가 조회되지 않습니다.");
        modifyUserRole(member, UserRole.ROLE_USER);
        redisUtil.deleteData(key);
    }

    @Override
    public void modifyUserRole(Member member, UserRole userRole) {
        member.setRole(userRole);
        memberRepository.save(member);
    }

    @Override
    public Member findByUsername(String username) throws NotFoundException {
        Member member = memberRepository.findByUsername(username);

        if (member == null)
            throw new NotFoundException("사용자가 조회되지 않습니다.");
        return member;
    }

    @Override
    public void sendVerificationMail(Member member) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/verify/";
        if (member == null)
            throw new NotFoundException("사용자가 조회되지 않습니다.");
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(), member.getUsername(), 60 * 30L);
        emailService.sendMail(member.getEmail(), "SADANG 인증 메일입니다.",VERIFICATION_LINK + uuid.toString());

    }


}
