package com.market.sadang.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Response;
import com.market.sadang.domain.SecurityMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(200);
        response.setContentType("application/json;charset=utf-8");
        Response res = new Response("error","접근 가능한 권한을 가지고 있지 않습니다.",null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember member = (SecurityMember) authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = member.getAuthorities();

        if (hasRole(authorities, UserRole.ROLE_NOT_PERMITTED.name())){
//            res.setMessage("사용자 인증메일을 받지 않았습니다.");
            response.sendRedirect("/signup");
        }

        /*PrintWriter out = response.getWriter();
        String jsonResponse = objectMapper.writeValueAsString(res);
        out.print(jsonResponse);*/
    }

    private boolean hasRole(Collection<GrantedAuthority> authorities, String role) {
        return authorities.contains(new SimpleGrantedAuthority(role));
    }
}