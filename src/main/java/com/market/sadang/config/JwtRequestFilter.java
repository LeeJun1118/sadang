package com.market.sadang.config;

import com.market.sadang.domain.Member;
import com.market.sadang.service.CookieUtil;
import com.market.sadang.service.JwtUtil;
import com.market.sadang.service.MyUserDetailService;
import com.market.sadang.service.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
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
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MyUserDetailService userDetailService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final Cookie jwtToken = cookieUtil.getCookie(request,JwtUtil.ACCESS_TOKEN_NAME);

        String userId = null;
        String jwt = null;
        String refreshJwt = null;
        String refreshUname = null;

        try {
            if(jwtToken != null){
                jwt = jwtToken.getValue();
                userId = jwtUtil.getUserId(jwt);
            }
            if (userId != null){
                UserDetails userDetails = userDetailService.loadUserByUsername(userId);

                if (jwtUtil.validateToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }catch (ExpiredJwtException e){
            Cookie refreshToken = cookieUtil.getCookie(request, JwtUtil.REFRESH_TOKEN_NAME);
            if (refreshToken != null){
                refreshJwt = refreshToken.getValue();
            }
        }catch (Exception e){

        }

        try {
            if(refreshJwt != null){
                refreshUname = redisUtil.getData(refreshJwt);

                if (refreshUname.equals(jwtUtil.getUserId(refreshJwt))){
                    UserDetails userDetails = userDetailService.loadUserByUsername(refreshUname);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    Member member = new Member();
                    member.setUsername(refreshUname);
                    String newToken = jwtUtil.generateToken(member);

                    Cookie newAccessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME,newToken);
                    response.addCookie(newAccessToken);
                }
            }
        }catch (ExpiredJwtException e){

        }
        filterChain.doFilter(request,response);
    }
}
