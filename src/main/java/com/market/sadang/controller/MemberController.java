package com.market.sadang.controller;


import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.Response;
import com.market.sadang.domain.SignUpForm;
import com.market.sadang.domain.request.RequestLoginUser;
import com.market.sadang.domain.request.RequestVerifyUser;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.AuthService;
import com.market.sadang.service.CookieUtil;
import com.market.sadang.service.JwtUtil;
import com.market.sadang.service.RedisUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.model.IModel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor

//ResponseBody 를 모든 메소드에 적용해줌
@RestController
//@Controller
public class MemberController {

    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @GetMapping("/signup")
//    public String signUpUser(Model model) {
    public ModelAndView signUpUser(ModelAndView model) {
//        model.addAttribute("signUpForm", new SignUpForm());
        model.addObject("signUpForm", new SignUpForm());
        model.setViewName("auth/signUpPage");
        return model;
//        return "auth/signUpPage";
    }

    @PostMapping(value = "/signup")
//    public String signUpUser(SignUpForm signUpForm, Model model) {
    public ModelAndView signUpUser(SignUpForm signUpForm, ModelAndView model) {
//        model.addAttribute("member", signUpForm);
        model.addObject("member", signUpForm);
        model.setViewName("auth/mailVerify");

//        return "auth/mailVerify";
        return model;
    }

    @PostMapping("/idCheck")
    public int idCheck(@RequestBody RequestVerifyUser username){
        // ajax에서 username=testUsername 이런식으로 받아와짐
        Map<String, Object> object = new HashMap<String, Object>();
        int countUser = memberRepository.countByUsername(username.getUsername());

        System.out.println("username==" + username.getUsername());
        System.out.println("countUser==" + countUser);

        return countUser;
    }

    @PostMapping("/login")
    public Response login(RequestLoginUser user,
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
//    public String verify(Member member,
    public ModelAndView verify(Member member,
                               HttpServletRequest req, HttpServletResponse res,
//                         Model model) {
                               ModelAndView model) {
        Response response;

        try {
            // 회원 가입
            authService.signUpUser(member);

            //메일 보냄
            authService.sendVerificationMail(member);

            RequestVerifyUser verifyUser = new RequestVerifyUser();
            verifyUser.setUsername(member.getUsername());
//            model.addAttribute("verifyUser", verifyUser);
            model.addObject("username", verifyUser.getUsername());

            response = new Response("success", "성공적으로 인증메일을 보냈습니다.", null);
            System.out.println(response.getMessage());
        } catch (Exception e) {
            response = new Response("error", "인증메일을 보내는데 실패했습니다.", e);
            System.out.println(response.getMessage());
        }

        model.setViewName("auth/mailConfirm");
        return model;
//        return "auth/mailConfirm";
    }

    @GetMapping("/verify/{key}")
//    public String getVerify(@PathVariable String key) {
    public ModelAndView getVerify(@PathVariable String key, ModelAndView modelAndView) {
        Response response;
        try {
            authService.verifyEmail(key);
            modelAndView.setViewName("auth/verify");
            return modelAndView;
        } catch (Exception e) {
            modelAndView.setViewName("auth/expired");
            return modelAndView;
//            return "auth/expired";
        }
    }

    @PostMapping("/confirm")
    public Object mailConfirm(@RequestBody RequestVerifyUser username) throws Exception {

        // ajax에서 username=testUsername 이런식으로 받아와짐
        String name = username.getUsername().split("=")[1];

        Map<String, Object> object = new HashMap<String, Object>();

        Member member = authService.findByUsername(name);

        System.out.println(member.getUsername());
        if (member.getUsername() != null && member.getRole() == UserRole.ROLE_USER) {
            System.out.println("member.name()" + member.getUsername());
            System.out.println("member.getRole()=" + member.getRole());
            object.put("responseCode", "success");
        }
        else {
            System.out.println("member.name()" + member.getUsername());
            System.out.println("member.getRole()=" + member.getRole());
            object.put("responseCode", "error");
            throw new Exception("메일 인증 안함");
        }
        return object;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("auth/loginPage");
        return modelAndView;
//        return "auth/loginPage";
    }
}

