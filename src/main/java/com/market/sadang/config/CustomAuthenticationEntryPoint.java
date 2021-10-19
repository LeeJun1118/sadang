package com.market.sadang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.sadang.domain.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(200);
        response.setContentType("application/json;charset=utf-8");
        response.sendRedirect("/login");
        /*Response res = new Response("error","로그인이 되지 않은 사용자입니다.",null);
        PrintWriter out = response.getWriter();
        String jsonResponse = objectMapper.writeValueAsString(response);
        out.print(jsonResponse);*/
    }
}
