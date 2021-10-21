package com.market.sadang.service.board;

import com.market.sadang.domain.Member;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.authUtil.CookieUtil;
import com.market.sadang.service.authUtil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        String memberId = jwtUtil.getUserId(jwtToken.getValue());

        return memberRepository.findByUserId(memberId);
    }
}
