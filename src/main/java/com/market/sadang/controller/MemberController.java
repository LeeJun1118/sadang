package com.market.sadang.controller;


import com.market.sadang.domain.Member;
import com.market.sadang.domain.Response;
import com.market.sadang.domain.SignUpForm;
import com.market.sadang.domain.request.RequestLoginUser;
import com.market.sadang.domain.request.RequestVerifyEmail;
import com.market.sadang.service.AuthService;
import com.market.sadang.service.CookieUtil;
import com.market.sadang.service.JwtUtil;
import com.market.sadang.service.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @GetMapping("/signup")
    public ModelAndView signUpUser(ModelAndView modelAndView) {
        modelAndView.addObject("signUpForm", new SignUpForm());
        modelAndView.setViewName("auth/signUpPage");
        return modelAndView;
    }

    @PostMapping("/signup")
    public Response signUpUser(@RequestBody Member member, ModelAndView modelAndView) {
        Response response = new Response();

        try {
            authService.signUpUser(member);
            response.setResponse("success");
            response.setMessage("회원가입 성공");

            modelAndView.setViewName("index");
        } catch (Exception e) {
            response.setResponse("failed");

            response.setData(e.toString());
            System.out.println(e.getMessage());
        }
        return response;
    }

    @PostMapping("/login")
    public Response login(@RequestBody RequestLoginUser user,
                          HttpServletRequest req,
                          HttpServletResponse res) {
        try {
            final Member member = authService.loginUser(user.getUsername(), user.getPassword());
            final String token = jwtUtil.generateToken(member);
            final String refreshJwt = jwtUtil.generateRefreshToken(member);

            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);

            redisUtil.setDataExpire(refreshJwt, member.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            res.addCookie(accessToken);
            res.addCookie(refreshToken);

            return new Response("success", "로그인 성공", token);
        } catch (Exception e) {
            return new Response("error", "로그인 실패", e.getMessage());
        }
    }

    @PostMapping("/verify")
    public Response verify(@RequestBody RequestVerifyEmail requestVerifyEmail,
                           HttpServletRequest req, HttpServletResponse res) {
        Response response;

        try {
            Member member = authService.findByUsername(requestVerifyEmail.getUsername());
            authService.sendVerificationMail(member);
            response = new Response("success", "성공적으로 인증메일을 보냈습니다.", null);
            System.out.println(response);
        } catch (Exception e) {
            response = new Response("error", "인증메일을 보내는데 실패했습니다.", e);
            System.out.println(response);
        }
        return response;
    }

    @GetMapping("/verify/{key}")
    public ModelAndView getVerify(@PathVariable String key, ModelAndView modelAndView) {
        Response response;
        try {
            authService.verifyEmail(key);
            modelAndView.setViewName("auth/verify");
        } catch (Exception e) {
            modelAndView.setViewName("auth/expired");
        }
        return modelAndView;
    }

}
