package com.market.sadang.controller;


import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.Response;
import com.market.sadang.domain.SignUpForm;
import com.market.sadang.domain.request.RequestLoginUser;
import com.market.sadang.domain.request.RequestVerifyUser;
import com.market.sadang.service.AuthService;
import com.market.sadang.service.CookieUtil;
import com.market.sadang.service.JwtUtil;
import com.market.sadang.service.RedisUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @GetMapping("/signup")
    public String signUpUser(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
//        model.setViewName("auth/signUpPage");
        return "auth/signUpPage";
    }

    @PostMapping(value = "/signup")
    public String signUpUser(SignUpForm signUpForm, Model model) {
        model.addAttribute("member", signUpForm);
       /* Response response = new Response();

        try {
            //Member member = authService.signUpUser(signUpForm);
            Member member = new Member();
            member.setUsername(signUpForm.getUsername());
            member.setPassword(signUpForm.getPassword());
            member.setAddress(signUpForm.getAddress());
            model.addAttribute("member", signUpForm);
            response.setResponse("success");
            response.setMessage("회원가입 성공");

        } catch (Exception e) {
            response.setResponse("failed");

            response.setData(e.toString());
            System.out.println(e.getMessage());
        }*/
        return "auth/mailVerify";
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
    public String verify(Member member,
                         HttpServletRequest req, HttpServletResponse res,
                         Model model) {
        Response response;

        try {
            // 회원 가입
            authService.signUpUser(member);

            //메일 보냄
            authService.sendVerificationMail(member);

            RequestVerifyUser verifyUser = new RequestVerifyUser();
            verifyUser.setUsername(member.getUsername());
            model.addAttribute("verifyUser", verifyUser);

            response = new Response("success", "성공적으로 인증메일을 보냈습니다.", null);
            System.out.println(response);
        } catch (Exception e) {
            response = new Response("error", "인증메일을 보내는데 실패했습니다.", e);
            System.out.println(response);
        }
        return "auth/mailConfirm";
    }

    @GetMapping("/verify/{key}")
    public String getVerify(@PathVariable String key) {
        Response response;
        try {
            authService.verifyEmail(key);
            return "auth/verify";
        } catch (Exception e) {
            return "auth/expired";
        }
    }

    @PostMapping("/confirm")
    public String mailConfirm(RequestVerifyUser username) throws NotFoundException {
        Member member = authService.findByUsername(username.getUsername());
        System.out.println(username.getUsername());
        System.out.println("member role : "+ member.getRole());
        System.out.println(UserRole.ROLE_USER);
        if (member.getRole() == UserRole.ROLE_USER)
        return "auth/loginPage";

        else return "redirect:/";
    }

}
