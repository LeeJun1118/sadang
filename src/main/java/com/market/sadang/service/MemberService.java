package com.market.sadang.service;

import com.market.sadang.domain.Member;
import com.market.sadang.dto.member.MemberUpdateRequestDto;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.authUtil.CookieUtil;
import com.market.sadang.service.authUtil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    //사용자 찾기
    public Member searchMemberId(HttpServletRequest request) {
        Cookie jwtToken = cookieUtil.getCookie(request, "accessToken");

        try {
            String memberId = jwtUtil.getUsername(jwtToken.getValue());
            return memberRepository.findByUsername(memberId);

        } catch (Exception e) {
            System.out.println("searchMemberId() ERROR : " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public void update(MemberUpdateRequestDto requestDto, Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        member.update(
                requestDto.getName()
                , requestDto.getUsername()
                , requestDto.getEmail()
                , requestDto.getAddress()
                , requestDto.getDetailAddress());
    }

    public Member findById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
    }
}
