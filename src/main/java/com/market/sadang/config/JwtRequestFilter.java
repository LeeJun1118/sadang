package com.market.sadang.config;

import com.market.sadang.domain.Member;
import com.market.sadang.service.MemberService;
import com.market.sadang.service.authUtil.CookieUtil;
import com.market.sadang.service.authUtil.JwtUtil;
import com.market.sadang.service.authUtil.MyUserDetailService;
import com.market.sadang.service.authUtil.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MyUserDetailService userDetailService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final Cookie jwtToken = cookieUtil.getCookie(request, JwtUtil.ACCESS_TOKEN_NAME);

        HttpSession session = request.getSession();


        String username = null;
        String jwt = null;
        String refreshJwt = null;
        String refreshUname = null;

        //ACCESS_TOKEN 토큰 검증
        try {
//            if (jwtToken != null) {
            if (session != null) {
                Member member = memberService.findByMemberRequest(request);
//                jwt = jwtToken.getValue();
//                username = jwtUtil.getUsername(jwt);
                username = member.getUsername();
            }
            if (username != null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

//                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                }
            }
        }
        //ACCESS_TOKEN 토큰 만료 시 REFRESH_TOKEN 받아옴
        /*catch (ExpiredJwtException e) {
            Cookie refreshToken = cookieUtil.getCookie(request, JwtUtil.REFRESH_TOKEN_NAME);
            if (refreshToken != null) {
                refreshJwt = refreshToken.getValue();
            }
        } */catch (Exception e) {

        }

      /*  try {
            // REFRESH_TOKEN 이 있다면
            if (refreshJwt != null) {
                // Redis 에서 REFRESH_TOKEN 으로 사용자 이름 받아옴
                refreshUname = redisUtil.getData(refreshJwt);

                // REFRESH_TOKEN 의 payload 에 있는 사용자 이름과 redis 에 있는 사용자 이름이 같다면
                if (refreshUname.equals(jwtUtil.getUsername(refreshJwt))) {

                    // PostrgreSQL 에서 사용자 찾아옴
                    UserDetails userDetails = userDetailService.loadUserByUsername(refreshUname);

                    if (userDetails == null)
                        throw new Exception("사용자가 존재하지 않음");

                    // 아이디와 비밀번호를 기반으로 UserPasswordAuthenticationToken을 발급 = Authentication 객체
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Authentication 객체에 HttpServletRequest 정보를 저장
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Authentication 객체 저장
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    // 사용자의 새로운 AccessToken 발급
                    Member member = new Member();
                    member.setUsername(refreshUname);
                    String newToken = jwtUtil.generateToken(member);

                    Cookie newAccessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, newToken);
                    response.addCookie(newAccessToken);
                }
            }
        } catch (ExpiredJwtException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        filterChain.doFilter(request, response);
    }
}
